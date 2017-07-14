package com.peter.zensleepfree.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Camera;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.peter.zensleepfree.CustomView.BubblingView;
import com.peter.zensleepfree.CustomView.FontTextView;
import com.peter.zensleepfree.Interfaces.PlugInInterface;
import com.peter.zensleepfree.Model.SleepDBModel;
import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.Analytics;
import com.peter.zensleepfree.UtilsClass.Constant;
import com.peter.zensleepfree.UtilsClass.SleepDBHelper;
import com.peter.zensleepfree.UtilsClass.Utils;
import com.peter.zensleepfree.UtilsClass.ZenSleep;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("deprecation")
public class SleepActivity extends AppCompatActivity {

    private static final int NUM_BAR = 100;
    private BubblingView bubblingView;
    private Timer bubblingTimer = new Timer();
    private Timer cutTimer = new Timer();
    private Timer getTimeTimer = new Timer();
    private Timer animationTimer = new Timer();

    private Animation in, out;
    private FontTextView btnStop, txtCurrentTime, txtAMPM, txtAlarmTime, txtPowerGuide;
    private ImageView btnDream, btnFlash, imgPowerAnimator;
    private Button btnSnooze;


    private Camera cam = null;
    private boolean isFlashOn = false;

    private static final int RECORDER_SAMPLE_RATE = 44100;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    private AudioRecord recorder = null;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    private int BufferElements2Rec = 1024 * 2; // want to play 2048 (2K) since 2 bytes we use only 1024
    private int BytesPerElement = 2;
    private boolean isRecordBegin = false;

    private ArrayList<short[]> bufferList = new ArrayList<>();
    private SleepDBHelper sleepDBHelper;
    private int startTimeSec = 0;
    private int endTimeSec = 0;
    private int elapsedTimeSec = 0;
    private ArrayList<SleepDBModel> sleepDatas = new ArrayList<>();

    private boolean isAM = false;
    private String currentHour, currentMin;
    private boolean canFireAlarm = true;

    private MediaPlayer mediaPlayer;

    private AdView admobAdView;
    private PlugInInterface callBack;

    private Handler snoozeHandler = new Handler();
    private Runnable snoozeAction;

    private int[] images = { R.drawable.power_00001, R.drawable.power_00001, R.drawable.power_00002, R.drawable.power_00003, R.drawable.power_00004,
            R.drawable.power_00005, R.drawable.power_00006, R.drawable.power_00007, R.drawable.power_00008, R.drawable.power_00009,
            R.drawable.power_00010, R.drawable.power_00011, R.drawable.power_00012, R.drawable.power_00013, R.drawable.power_00014,
            R.drawable.power_00015, R.drawable.power_00016, R.drawable.power_00017, R.drawable.power_00018, R.drawable.power_00019,
            R.drawable.power_00020, R.drawable.power_00021, R.drawable.power_00022, R.drawable.power_00023, R.drawable.power_00024,
            R.drawable.power_00025, R.drawable.power_00026, R.drawable.power_00027, R.drawable.power_00028 };
    private int imageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sleep);

        bubblingView = (BubblingView) findViewById(R.id.sleep_bubbling);
        btnStop = (FontTextView) findViewById(R.id.btn_stop);
        btnDream = (ImageView) findViewById(R.id.btn_dream);
        btnFlash = (ImageView) findViewById(R.id.btn_flash);
        txtAlarmTime = (FontTextView) findViewById(R.id.text_alarm_time);
        txtCurrentTime = (FontTextView) findViewById(R.id.text_current_time);
        txtAMPM = (FontTextView) findViewById(R.id.text_am_pm);
        imgPowerAnimator = (ImageView) findViewById(R.id.power_animator);
        btnSnooze = (Button) findViewById(R.id.btn_sleep_alarm_snooze);
        txtPowerGuide = (FontTextView) findViewById(R.id.text_sleep_power_guide);

        bubblingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshView();
            }
        }, 0, 50);

        initUI();
        startAnimation();

        cutTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                cutAudioData();
            }
        }, 0, 1000 * Constant.BUFFER_INTERVAL_SECONDS);

        getTimeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCurrentTime();
                    }
                });
            }
        }, 0, 1000);

        ZenSleep.sleepStatus.clear();
        sleepDBHelper = new SleepDBHelper(this);
        sleepDatas = sleepDBHelper.getAllData();
        startTimeSec = (int) (System.currentTimeMillis()/1000);

        admobAdView = (AdView) findViewById(R.id.sleep_activity_banner_ad);

        beginPowerAnimation();
    }

    private void initUI() {

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSleep();
            }
        });

        if (Utils.getEnableAlarm(this)) {
            txtAlarmTime.setText(ZenSleep.alarmTimeText);
        } else {
            txtAlarmTime.setVisibility(View.INVISIBLE);
        }

        btnDream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SleepActivity.this, DreamActivity.class));
            }
        });

        btnSnooze.setVisibility(View.GONE);
        btnSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAlarm();
                btnSnooze.setVisibility(View.GONE);
                snoozeAction = new Runnable() {
                    @Override
                    public void run() {
                        playAlarm();
                    }
                };
                snoozeHandler.postDelayed(snoozeAction, Utils.getSnoozeCoolTime(SleepActivity.this) * 60 * 1000);
            }
        });
    }

    private void stopSleep() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure want to quite?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        stopRecording();
                        stopAlarm();
                        endMeasuringSleepStatus();
                        if (elapsedTimeSec >= 10 * 60 ) {
                            Intent intent = new Intent(SleepActivity.this, SleepResultActivity.class);
                            startActivity(intent);
                            snoozeHandler.removeCallbacks(snoozeAction);
                            finish();
                        } else {
                            releaseAllTimers();
                            snoozeHandler.removeCallbacks(snoozeAction);
                            finish();
                        }
                    }
                }).show();
    }

    public void refreshView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bubblingView.invalidate();
            }
        });
    }

    private void startAnimation() {
        in = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                btnStop.startAnimation(out);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        btnStop.startAnimation(in);
        out = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                btnStop.startAnimation(in);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        btnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    turnOffFlashLight();
                    isFlashOn = false;
                } else {
                    turnOnFlashLight();
                    isFlashOn = true;
                }
            }
        });
    }

    private void beginPowerAnimation() {
        if (!ZenSleep.powerConnected) {
            animationTimer = new Timer();
            imageIndex = 0;
            animationTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    refreshImage();
                }
            }, 0, 50);
        }
    }

    private void stopPowerAnimation() {
        animationTimer = null;
    }

    private void refreshImage() {

        if (!ZenSleep.powerConnected) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtPowerGuide.setVisibility(View.VISIBLE);
                    imgPowerAnimator.setVisibility(View.VISIBLE);
                    if (imageIndex < images.length) {
                        imgPowerAnimator.setImageResource(images[imageIndex]);
                        imageIndex++;
                    } else {
                        imageIndex = 0;
                    }
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imgPowerAnimator.setVisibility(View.INVISIBLE);
                    txtPowerGuide.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    private void showAdmobBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();
        // Start loading the ad in the background.
        admobAdView.loadAd(adRequest);
    }

    private void showCurrentTime() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        if (!Utils.is12(this)) {
            txtAMPM.setVisibility(View.INVISIBLE);
            if (hour > 9) {
                currentHour = String.valueOf(hour);
            } else {
                currentHour = "0" + String.valueOf(hour);
            }
        } else {
            txtAMPM.setVisibility(View.VISIBLE);
            if (hour > 12) {
                if (hour - 12 < 10) {
                    currentHour = "0" + String.valueOf(hour - 12);
                } else {
                    currentHour = String.valueOf(hour - 12);
                }

                txtAMPM.setText("PM");
                isAM = false;
            } else {
                txtAMPM.setText("AM");
                if (hour > 9) {
                    currentHour = String.valueOf(hour);
                } else {
                    currentHour = "0" + String.valueOf(hour);
                }
                isAM = true;
            }
        }
        if (minute > 9) {
            currentMin = String.valueOf(minute);
        } else {
            currentMin = "0" + String.valueOf(minute);
        }
        txtCurrentTime.setText(currentHour + ":" + currentMin);
        setGradientBackground();
        fireAlarm(hour, minute);
    }

    private void fireAlarm(int hour, int minute) {
        if (ZenSleep.alarmHour == hour && ZenSleep.alarmMin <= minute && canFireAlarm && Utils.getEnableAlarm(this)) {
            stopRecording();
            endMeasuringSleepStatus();
            playAlarm();
            canFireAlarm = false;
        }
    }

    private void playAlarm() {
        String fileName = Utils.getAlarmSound(this);
        int resID = getResources().getIdentifier(fileName.toLowerCase(), "raw", getPackageName());
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        mediaPlayer = MediaPlayer.create(this, resID);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        btnSnooze.setVisibility(View.VISIBLE);
        imgPowerAnimator.setVisibility(View.GONE);
        txtPowerGuide.setVisibility(View.GONE);
    }

    private void stopAlarm() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void turnOnFlashLight() {
        try {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                cam = Camera.open();
                Camera.Parameters p = cam.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                cam.setParameters(p);
                cam.startPreview();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void turnOffFlashLight() {
        try {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                cam.stopPreview();
                cam.release();
                cam = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startRecording() {

        bufferList.clear();

        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
                RECORDER_SAMPLE_RATE, RECORDER_CHANNELS,
                RECORDER_AUDIO_ENCODING, BufferElements2Rec * BytesPerElement);
        int state = recorder.getState();
        if (state == AudioRecord.STATE_INITIALIZED) {
            recorder.startRecording();
            isRecording = true;
            recordingThread = new Thread(new Runnable() {
                public void run() {
                    writeAudioDataToArray();
                }
            }, "AudioRecorder Thread");
            recordingThread.start();
        }
    }

    private void writeAudioDataToArray() {
        while (isRecording) {
            // gets the voice output from microphone to byte format
            short sData[] = new short[BufferElements2Rec];
            int readedSize = recorder.read(sData, 0, BufferElements2Rec);

//            Log.d("Read Audio Data Size", String.valueOf(readedSize));
            bufferList.add(sData);
        }
    }

    private void stopRecording() {
        // stops the recording activity
        if (null != recorder && isRecording) {
            isRecording = false;
            recorder.stop();
            recorder.release();
            recorder = null;
            recordingThread = null;
        }
    }
    private void cutAudioData() {
        if (isRecordBegin) {
            stopRecording();
            Log.d("Record complete, Size ", String.valueOf(bufferList.size()));
            Analytics analytics = new Analytics(bufferList);
            analytics.beingAnalyzing();
            bufferList.clear();
        }
        isRecordBegin = true;
        //Restart recording
        startRecording();
    }
    private void endMeasuringSleepStatus() {
        endTimeSec = (int) (System.currentTimeMillis()/1000);
        elapsedTimeSec = endTimeSec - startTimeSec;
        if (elapsedTimeSec >= 10 * 60) {
            SleepDBModel sleepDBModel = new SleepDBModel();
            sleepDBModel.setId(String.valueOf(sleepDatas.size()));
            sleepDBModel.setStatus(ZenSleep.sleepConditions);
            sleepDBModel.setStartTimeSec(startTimeSec);
            sleepDBModel.setElapsedSec(elapsedTimeSec);
            sleepDBModel.setStagesLotSec(Constant.BUFFER_INTERVAL_SECONDS);
            correctStatus();
            sleepDBModel.setStages(convertArrayListToArray(ZenSleep.sleepStatus));

            ZenSleep.tempModel = sleepDBModel;
            releaseAllTimers();
        }
    }

    private void correctStatus() {
        ArrayList<Float> bar = new ArrayList<>();
        int nCnt = ZenSleep.sleepStatus.size();
        if (nCnt > 0) {
            ArrayList<Float> buffer = new ArrayList<>();
            for (int i = 0; i < nCnt; i++) {
                float d = ZenSleep.sleepStatus.get(i);
                if (d > 1) d = 1;
                if (d < 0) d = 0;
                buffer.add(d);
            }

            float secPerBar = elapsedTimeSec/NUM_BAR;
            float sum;
            int curSec = 0;
            for (int i = 0; i < NUM_BAR; i++) {
                sum = 0;
                for (int sec = 0; sec < secPerBar; sec++) {
                    int idx = (curSec + sec)/Constant.BUFFER_INTERVAL_SECONDS;
                    if (idx > nCnt - 1) idx = nCnt - 1;
                    sum += buffer.get(idx);
                }
                bar.add(sum/secPerBar);
                curSec += secPerBar;
            }

            for (int i = 1; i < NUM_BAR; i++) {
                float d1 = bar.get(i - 1);
                float d2 = bar.get(i);

                int l1 = levelForValue(d1);
                int l2 = levelForValue(d2);

                if (Math.abs(l1 - l2) >1) {
                    d2 = (d1 + d2)/2;
                    bar.set(i, d2);
                }
            }

            for (int i = 1; i < NUM_BAR - 1; i++) {
                float d1 = bar.get(i - 1);
                float d2 = bar.get(i);
                float d3 = bar.get(i + 1);

                int l1 = levelForValue(d1);
                int l2 = levelForValue(d2);
                int l3 = levelForValue(d3);

                if( l1 == l3 && l1 != l2 )
                {
                    d2 = (d1+d3)/2.0f;
                    bar.set(i, d2);
                }
            }
            ZenSleep.sleepStatus.clear();
            for (int i = 0; i < NUM_BAR; i++) {
                ZenSleep.sleepStatus.add(bar.get(i));
            }
        }
    }

    private void releaseAllTimers() {
        if (bubblingTimer != null) {
            bubblingTimer.cancel();
            bubblingTimer = null;
        }
        if (cutTimer != null) {
            cutTimer.cancel();
            cutTimer = null;
        }
        if (getTimeTimer != null) {
            getTimeTimer.cancel();
            getTimeTimer = null;
        }
    }

    private float[] convertArrayListToArray(ArrayList<Float> arrayList) {
        float[] result = new float[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            result[i] = arrayList.get(i).floatValue();
        }
        return result;
    }

    private void setGradientBackground() {
        float timeValue;
        if (Utils.is12(this)) {
            if (isAM) {
                timeValue = Float.parseFloat(currentHour) + Float.parseFloat(currentMin) / 60.0f;
            } else {
                timeValue = Float.parseFloat(currentHour) + 12.0f + Float.parseFloat(currentMin) / 60.0f;
            }
        } else {
            timeValue = Float.parseFloat(currentHour) + Float.parseFloat(currentMin) / 60.0f;
        }
        if (timeValue >= 24) {
            timeValue = 24.0f;
        } else if (timeValue <= 0) {
            timeValue = 0.0f;
        }

        int topColor = Utils.getTopColor(timeValue);
        int bottomColor = Utils.getBottomColor(timeValue);

        int[] colors = {topColor, bottomColor};
        //create a new gradient color
        GradientDrawable gd = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM, colors);
        bubblingView.setBackground(gd);
    }

    private int levelForValue(float d) {
        int level = 0;
        if (d >= 0.65) {
            level = 2;
        } else if (d >= 0.3) {
            level = 1;
        }

        return level;
    }
//    - (int)levelForValue:(double)d
//    {
//        int level = 0;
//        if( d >= [Sleep awakeThreshold] )
//        level = 2;
//        else if( d >= [Sleep lightSleepThreshold] )
//        level = 1;
//        return level;
//    }

    @Override
    protected void onResume() {
        super.onResume();
        showAdmobBanner();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
