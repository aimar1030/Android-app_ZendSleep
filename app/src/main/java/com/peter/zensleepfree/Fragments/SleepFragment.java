package com.peter.zensleepfree.Fragments;


import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.peter.zensleepfree.CustomView.BubblingView;
import com.peter.zensleepfree.CustomView.FontTextView;
import com.peter.zensleepfree.CustomView.InfiniteScrollView;
import com.peter.zensleepfree.Interfaces.CustomButtonClickListener;
import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.Utils;
import com.peter.zensleepfree.UtilsClass.ZenSleep;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class SleepFragment extends Fragment implements NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener, View.OnClickListener {


    private static final String TAG = "Sleep Fragment";
    private BubblingView bubblingView;
    private InfiniteScrollView hourPicker, minPicker;
    private FontTextView colon, txtHour, txtMin, txtAM, txtAlarmText, btnStart;
    private ImageView imgAlarm;
    private Timer bubblingTimer = new Timer();
    private boolean isAM = false;
    private Animation in, out;
    private static CustomButtonClickListener buttonClickListener;

    private Handler handler = new Handler();

    public SleepFragment() {
        // Required empty public constructor
    }

    public static SleepFragment newInstance(CustomButtonClickListener callBack) {

        SleepFragment fragment = new SleepFragment();
        buttonClickListener = callBack;
        ZenSleep.sleepFinished = false;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sleep, container, false);

        bubblingView = (BubblingView) view.findViewById(R.id.bubble_view);
        hourPicker = (InfiniteScrollView) view.findViewById(R.id.hour_picker);
        minPicker = (InfiniteScrollView) view.findViewById(R.id.min_picker);
        imgAlarm = (ImageView) view.findViewById(R.id.alarm_icon);
        colon = (FontTextView) view.findViewById(R.id.colon);
        txtHour = (FontTextView) view.findViewById(R.id.txt_hour);
        txtMin = (FontTextView) view.findViewById(R.id.txt_minute);
        txtAlarmText = (FontTextView) view.findViewById(R.id.alarm_text);
        txtAM = (FontTextView) view.findViewById(R.id.am_pm);
        btnStart = (FontTextView) view.findViewById(R.id.btn_start);

        initUI();
        startAnimation();

        return view;
    }

    public void refreshView() {
        if (getActivity() == null) {
            return ;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bubblingView.invalidate();
            }
        });
    }

    private void initUI () {
        bubblingTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshView();
            }
        }, 0, 50);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "Gotham-Thin.ttf");
        hourPicker.setTypeface(typeface);
        minPicker.setTypeface(typeface);
//
//        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        int oHour = Utils.getAlarmTimeHour(getActivity());
        int oMin = Utils.getAlarmTimeMin(getActivity());
        if (!Utils.is12(getActivity())) {
            hourPicker.setMaxValue(23);
            hourPicker.setMinValue(0);
            txtAM.setVisibility(View.INVISIBLE);
            if (oHour > 9) {
                txtHour.setText(String.valueOf(oHour));
            } else {
                txtHour.setText("0" + String.valueOf(oHour));
            }
            hourPicker.setValue(oHour);
        } else {
            hourPicker.setMaxValue(12);
            hourPicker.setMinValue(1);
            txtAM.setVisibility(View.VISIBLE);
            if (oHour > 12) {
                if (oHour - 12 < 10) {
                    txtHour.setText("0" + String.valueOf(oHour - 12));
                } else {
                    txtHour.setText(String.valueOf(oHour - 12));
                }

                hourPicker.setValue(oHour - 12);
                txtAM.setText("PM");
                isAM = false;
            } else {
                txtAM.setText("AM");
                if (oHour > 9) {
                    txtHour.setText(String.valueOf(oHour));
                } else {
                    txtHour.setText("0" + String.valueOf(oHour));
                }
                isAM = true;
                hourPicker.setValue(oHour);
            }
        }
        if (oMin > 9) {
            txtMin.setText(String.valueOf(oMin));
            minPicker.setValue(oMin);
        } else {
            txtMin.setText("0" + String.valueOf(oMin));
            minPicker.setValue(oMin);
        }
        hourPicker.setAlpha(0.0f);
        minPicker.setAlpha(0.0f);

        hourPicker.setOnValueChangedListener(this);
        minPicker.setOnValueChangedListener(this);

        hourPicker.setOnScrollListener(this);
        minPicker.setOnScrollListener(this);
        imgAlarm.setOnClickListener(this);
        btnStart.setOnClickListener(this);

        setGradientBackground();
        if (Utils.getEnableAlarm(getActivity())) {
            updateAlarmTime();
        }
    }

    private void startAnimation() {
        in = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_in);
        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                btnStart.startAnimation(out);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        btnStart.startAnimation(in);
        out = AnimationUtils.loadAnimation(getActivity(), R.anim.zoom_out);
        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                btnStart.startAnimation(in);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()) {
            case R.id.hour_picker:
                if (!Utils.is12(getActivity())) {
                    hourPicker.setMaxValue(23);
                    hourPicker.setMinValue(0);
                    txtAM.setVisibility(View.INVISIBLE);
                    if (newVal > 9) {
                        txtHour.setText(String.valueOf(newVal));
                    } else {
                        txtHour.setText("0" + String.valueOf(newVal));
                    }
                } else {
                    hourPicker.setMaxValue(12);
                    hourPicker.setMinValue(1);
                    txtAM.setVisibility(View.VISIBLE);

                    if (newVal >= 12) {
                        txtHour.setText(String.valueOf(newVal));
                    } else {
                        if (newVal > 9) {
                            txtHour.setText(String.valueOf(newVal));
                        } else {
                            txtHour.setText("0" + String.valueOf(newVal));
                        }
                    }
                    if ((oldVal == 11 && newVal == 12) || (oldVal == 0 && newVal == 12)) {
                        isAM = !isAM;
                    }
                    if (isAM) {
                        txtAM.setText("AM");
                    } else {
                        txtAM.setText("PM");
                    }
                }
                break;
            case R.id.min_picker:
                if (newVal > 9) {
                    txtMin.setText(String.valueOf(newVal));
                } else {
                    txtMin.setText("0" + String.valueOf(newVal));
                }
                break;
        }
        setGradientBackground();
        if (Utils.getEnableAlarm(getActivity())) {
            updateAlarmTime();
        }
    }

    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState) {
        switch (view.getId()){
            case R.id.hour_picker:
                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                        Log.d("Scroll Stat", "Fling");
                        break;
                    case SCROLL_STATE_IDLE:
                        Log.d("Scroll Stat", "Idle");
                        view.setAlpha(0.0f);
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        Log.d("Scroll Stat", "touch scroll");
                        view.setAlpha(1.0f);
                        break;
                }
                break;
            case R.id.min_picker:
                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                        Log.d("Scroll Stat", "Fling");
                        break;
                    case SCROLL_STATE_IDLE:
                        Log.d("Scroll Stat", "Idle");
                        view.setAlpha(0.0f);
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        Log.d("Scroll Stat", "touch scroll");
                        view.setAlpha(1.0f);
                        break;
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alarm_icon:
                Log.d(TAG, "Alarm Button Clicked");
                if (buttonClickListener != null) {
                    buttonClickListener.onAlarmClicked();
                }
                break;
            case R.id.btn_start:
                Log.d(TAG, "Start Button Clicked");
                if (buttonClickListener != null) {
                    buttonClickListener.onStartSleep();
                }
                break;
        }
    }

    private void updateAlarmTime() {
        int currentHour, currentMin, smartHour, smartMin, smartInterval;
        if (!Utils.getEnableSmartAlarm(getActivity())) {
            if (Utils.is12(getActivity())) {
                txtAlarmText.setText("alarm " + txtHour.getText().toString() + ":" + txtMin.getText().toString()  + " " + txtAM.getText().toString());
                if (isAM) {
                    currentHour = Integer.parseInt(txtHour.getText().toString());
                    currentMin = Integer.parseInt(txtMin.getText().toString());
                } else {
                    currentHour = Integer.parseInt(txtHour.getText().toString()) + 12;
                    currentMin = Integer.parseInt(txtMin.getText().toString());
                }
            } else {
                txtAlarmText.setText("alarm " + txtHour.getText().toString() + ":" + txtMin.getText().toString());
                currentHour = Integer.parseInt(txtHour.getText().toString());
                currentMin = Integer.parseInt(txtMin.getText().toString());
            }
            Log.d("Alarm Time", String.valueOf(currentHour) + ":" + String.valueOf(currentMin));
            ZenSleep.alarmHour = currentHour;
            ZenSleep.alarmMin = currentMin;
        } else {
            smartInterval = Utils.getSmartAlarmCoolTime(getActivity());
            if (Utils.is12(getActivity())) {
                if (isAM) {
                    currentHour = Integer.parseInt(txtHour.getText().toString());
                    currentMin = Integer.parseInt(txtMin.getText().toString());
                    if (currentMin - smartInterval >= 0) {
                        smartMin = currentMin - smartInterval;
                        smartHour = currentHour;
                    } else {
                        smartMin = 60  + currentMin - smartInterval;
                        if (currentHour - 1 == 0) {
                            smartHour = 12;
                        } else {
                            smartHour = currentHour - 1;
                        }
                    }
                    txtAlarmText.setText("alarm " + zeroPadding(smartHour) + ":" + zeroPadding(smartMin) + " AM" + " - " + txtHour.getText().toString() + ":" + txtMin.getText().toString() + " AM");
                } else {
                    currentHour = Integer.parseInt(txtHour.getText().toString()) + 12;
                    currentMin = Integer.parseInt(txtMin.getText().toString());
                    if (currentMin - smartInterval >= 0) {
                        smartMin = currentMin - smartInterval;
                        smartHour = currentHour;
                        txtAlarmText.setText("alarm " + zeroPadding(smartHour - 12) + ":" + zeroPadding(smartMin) + " PM" + " - " + txtHour.getText().toString() + ":" + txtMin.getText().toString() + " PM");
                    } else {
                        smartMin = 60  + currentMin - smartInterval;
                        smartHour = currentHour - 1;
                        if (smartHour - 12 == 0) {
                            txtAlarmText.setText("alarm " + "12" + ":" + zeroPadding(smartMin) + " PM" + " - " + txtHour.getText().toString() + ":" + txtMin.getText().toString() + " PM");
                        }   else {
                            txtAlarmText.setText("alarm " + zeroPadding(smartHour - 12) + ":" + zeroPadding(smartMin) + " PM" + " - " + txtHour.getText().toString() + ":" + txtMin.getText().toString() + " PM");
                        }
                    }
                }
            } else {
                currentHour = Integer.parseInt(txtHour.getText().toString());
                currentMin = Integer.parseInt(txtMin.getText().toString());
                if (currentMin - smartInterval >= 0) {
                    smartMin = currentMin - smartInterval;
                    smartHour = currentHour;
                } else {
                    smartMin = 60  + currentMin - smartInterval;
                    if (currentHour - 1 < 0) {
                        smartHour = 23;
                    } else {
                        smartHour = currentHour - 1;
                    }
                }
                txtAlarmText.setText("alarm " + zeroPadding(smartHour) + ":" + zeroPadding(smartMin) + " - " + txtHour.getText().toString() + ":" + txtMin.getText().toString());
            }
//            Log.d("Smart Alarm", String.valueOf(smartHour) + " : " + String.valueOf(smartMin));
            Log.d("Alarm Time", String.valueOf(currentHour) + ":" + String.valueOf(currentMin));
            ZenSleep.alarmHour = currentHour;
            ZenSleep.alarmMin = currentMin;
            ZenSleep.smartHour = smartHour;
            ZenSleep.smartMin = smartMin;
        }
        Utils.setAlarmTimeHour(getActivity(), ZenSleep.alarmHour);
        Utils.setAlarmTimeMin(getActivity(), ZenSleep.alarmMin);
        ZenSleep.alarmTimeText = txtAlarmText.getText().toString();
    }

    private void setGradientBackground() {
        float timeValue;
        if (Utils.is12(getActivity())) {
            if (isAM) {
                timeValue = Float.parseFloat(txtHour.getText().toString()) + Float.parseFloat(txtMin.getText().toString()) / 60.0f;
            } else {
                timeValue = Float.parseFloat(txtHour.getText().toString()) + 12.0f + Float.parseFloat(txtMin.getText().toString()) / 60.0f;
            }
        } else {
            timeValue = Float.parseFloat(txtHour.getText().toString()) + Float.parseFloat(txtMin.getText().toString()) / 60.0f;
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

//        (new Thread(){
//            @Override
//            public void run(){
//                for(int i=0; i<255; i++){
//                    handler.post(new Runnable(){
//                        public void run(){
//                            bubblingView.setBackgroundColor(Color.argb(255, i, i, i));
//                        }
//                    });
//                    // next will pause the thread for some time
//                    try {
//                        sleep(10);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
    }

    private String zeroPadding(int value) {
        if (value < 10) {
            return "0" + String.valueOf(value);
        } else {
            return String.valueOf(value);
        }
    }
}