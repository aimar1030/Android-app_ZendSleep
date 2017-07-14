package com.peter.zensleepfree.Fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.peter.zensleepfree.CustomView.FontTextView;
import com.peter.zensleepfree.CustomView.SleepDetailGraphTimeLineView;
import com.peter.zensleepfree.CustomView.SleepDetailGraphView;
import com.peter.zensleepfree.Interfaces.CustomButtonClickListener;
import com.peter.zensleepfree.Model.SleepDBModel;
import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.Constant;
import com.peter.zensleepfree.UtilsClass.Utils;

import java.util.ArrayList;
import java.util.Calendar;

import at.grabner.circleprogress.CircleProgressView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SleepDetailFragment extends Fragment implements View.OnClickListener {

    public static CustomButtonClickListener customButtonClickListener;
    public static SleepDBModel dataPosition;

    private FontTextView txtTitle, txtSleepDuration, txtAwakeDuration, txtLightDuration, txtDeepDuration, txtTotalEfficienty, txtAwakeEfficienty, txtLightEfficienty, txtDeepEfficienty;
    private ImageView btnShare, btnBack, imgMood, imgDream;
    private Button imgStatus1, imgStatus2, imgStatus3, imgStatus4, imgStatus5, imgStatus6;
    private EditText edtDreamNote;
    private SleepDetailGraphView graphView;
    private SleepDetailGraphTimeLineView timeLIneView;
    private CircleProgressView totalEfficienty, awakePercentage, lightPercentage, deepPercentage;
    private int NUMBER_TEXTS = 10;

    public SleepDetailFragment() {
        // Required empty public constructor
    }

    public static SleepDetailFragment newInstance(CustomButtonClickListener callBack, SleepDBModel data) {
        SleepDetailFragment fragment = new SleepDetailFragment();
        customButtonClickListener = callBack;
        dataPosition = data;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sleep_detail, container, false);

        txtTitle = (FontTextView) view.findViewById(R.id.sleep_detail_title);
        txtSleepDuration = (FontTextView) view.findViewById(R.id.sleep_detail_duration);
        txtAwakeDuration = (FontTextView) view.findViewById(R.id.text_sleep_detail_awake_duration);
        txtLightDuration = (FontTextView) view.findViewById(R.id.text_sleep_detail_light_duration);
        txtDeepDuration = (FontTextView) view.findViewById(R.id.text_sleep_deail_deep_duration);
        txtTotalEfficienty = (FontTextView) view.findViewById(R.id.text_sleep_detail_efficienty);
        txtAwakeEfficienty = (FontTextView) view.findViewById(R.id.text_sleep_detail_awake_efficienty);
        txtLightEfficienty = (FontTextView) view.findViewById(R.id.text_sleep_detail_light_efficienty);
        txtDeepEfficienty = (FontTextView) view.findViewById(R.id.text_sleep_detail_deep_efficienty);
        btnShare = (ImageView) view.findViewById(R.id.btn_sleep_detail_share);
        btnBack = (ImageView) view.findViewById(R.id.btn_sleep_detail_calendar);
        imgStatus1 = (Button) view.findViewById(R.id.sleep_detail_status_1);
        imgStatus2 = (Button) view.findViewById(R.id.sleep_detail_status_2);
        imgStatus3 = (Button) view.findViewById(R.id.sleep_detail_status_3);
        imgStatus4 = (Button) view.findViewById(R.id.sleep_detail_status_4);
        imgStatus5 = (Button) view.findViewById(R.id.sleep_detail_status_5);
        imgStatus6 = (Button) view.findViewById(R.id.sleep_detail_status_6);
        imgMood = (ImageView) view.findViewById(R.id.sleep_detail_mood);
        imgDream = (ImageView) view.findViewById(R.id.sleep_detail_dream);
        edtDreamNote = (EditText) view.findViewById(R.id.sleep_detail_dream_note);
        graphView = (SleepDetailGraphView) view.findViewById(R.id.sleep_detail_graph);
        totalEfficienty = (CircleProgressView) view.findViewById(R.id.sleep_detail_efficienty);
        awakePercentage = (CircleProgressView) view.findViewById(R.id.sleep_detail_awake);
        lightPercentage = (CircleProgressView) view.findViewById(R.id.sleep_detail_light_sleep);
        deepPercentage = (CircleProgressView) view.findViewById(R.id.sleep_detail_deep_sleep);
        timeLIneView = (SleepDetailGraphTimeLineView) view.findViewById(R.id.sleep_detail_time_line);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "Gotham-Light.ttf");
        edtDreamNote.setTypeface(typeface);

        btnShare.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        graphView.setFloatArrays(dataPosition.getStages());

        initUI();
        return view;
    }

    private  void initUI() {
        totalEfficienty.setValue(dataPosition.getEfficiency() * 100);
        int efficienty = (int) (dataPosition.getEfficiency() * 10000);
        txtTotalEfficienty.setText(String.valueOf(efficienty/100) + "%");

        float awake = 0.0f, light = 0.0f, deep = 0.0f;
        for (int i = 0; i < dataPosition.getStages().length; i++) {
            if (dataPosition.getStages()[i] > 0.66f) {
                awake++;
            } else if (dataPosition.getStages()[i] > 0.33f) {
                light++;
            } else {
                deep++;
            }
        }
        awakePercentage.setValue(awake/dataPosition.getStages().length * 100);
        txtAwakeEfficienty.setText(String.valueOf((int)(awake/dataPosition.getStages().length * 100)) + "%");
        lightPercentage.setValue(light/dataPosition.getStages().length * 100);
        txtLightEfficienty.setText(String.valueOf((int)(light/dataPosition.getStages().length * 100)) + "%");
        deepPercentage.setValue(deep/dataPosition.getStages().length * 100);
        txtDeepEfficienty.setText(String.valueOf((int)(deep/dataPosition.getStages().length * 100)) + "%");

        txtTitle.setText(Utils.getDateFromSec(dataPosition.getStartTimeSec(), "EEE, MMM d, yyyy"));
        txtSleepDuration.setText(Utils.getDateFromSec(dataPosition.getStartTimeSec(), "h:mm a")  + " - " + Utils.getDateFromSec(dataPosition.getStartTimeSec() + dataPosition.getElapsedSec(), "h:mm a"));
        int awakeSec = (int)(awake/100 * dataPosition.getElapsedSec());
        int lightSec = (int)(light/100 * dataPosition.getElapsedSec());
        int deepSec = (int)(deep/100 * dataPosition.getElapsedSec());
        txtAwakeDuration.setText(Utils.getHoursMinutes(awakeSec));
        txtLightDuration.setText(Utils.getHoursMinutes(lightSec));
        txtDeepDuration.setText(Utils.getHoursMinutes(deepSec));

        int bitAnd = dataPosition.getStatus() & 0x0001;
        if (bitAnd != 0) {
            imgStatus1.setSelected(true);
        } else {
            imgStatus1.setSelected(false);
        }
        bitAnd = dataPosition.getStatus() & 0x0002;
        if (bitAnd != 0) {
            imgStatus2.setSelected(true);
        } else {
            imgStatus2.setSelected(false);
        }

        bitAnd = dataPosition.getStatus() & 0x0004;
        if (bitAnd != 0) {
            imgStatus3.setSelected(true);
        } else {
            imgStatus3.setSelected(false);
        }

        bitAnd = dataPosition.getStatus() & 0x0008;
        if (bitAnd != 0) {
            imgStatus4.setSelected(true);
        } else {
            imgStatus4.setSelected(false);
        }

        bitAnd = dataPosition.getStatus() & 0x0010;
        if (bitAnd != 0) {
            imgStatus5.setSelected(true);
        } else {
            imgStatus5.setSelected(false);
        }

        bitAnd = dataPosition.getStatus() & 0x0020;
        if (bitAnd != 0) {
            imgStatus6.setSelected(true);
        } else {
            imgStatus6.setSelected(false);
        }

        edtDreamNote.setText(dataPosition.getNote());

        switch (dataPosition.getMood()) {
            case Constant.SLEEP_MOOD_1:
                imgMood.setImageResource(R.drawable.mood_1_on);
                break;
            case Constant.SLEEP_MOOD_2:
                imgMood.setImageResource(R.drawable.mood_2_on);
                break;
            case Constant.SLEEP_MOOD_3:
                imgMood.setImageResource(R.drawable.mood_3_on);
                break;
            case Constant.SLEEP_MOOD_4:
                imgMood.setImageResource(R.drawable.mood_4_on);
                break;
            case Constant.SLEEP_MOOD_5:
                imgMood.setImageResource(R.drawable.mood_5_on);
                break;
        }

        switch (dataPosition.getDream()) {
            case Constant.SLEEP_DREAM_BAD:
                imgDream.setImageResource(R.drawable.bad);
                break;
            case Constant.SLEEP_DREAM_PLEASANT:
                imgDream.setImageResource(R.drawable.pleasant);
                break;
            case Constant.SLEEP_DREAM_NEUTRAL:
                imgDream.setImageResource(R.drawable.neutral);
                break;
            case Constant.SLEEP_DREAM_NONE:
                break;
        }

        imgStatus1.setClickable(false);
        imgStatus2.setClickable(false);
        imgStatus3.setClickable(false);
        imgStatus4.setClickable(false);
        imgStatus5.setClickable(false);
        imgStatus6.setClickable(false);

        createTimeTexts();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sleep_detail_calendar:
                customButtonClickListener.onBackToNotesClicked();
                break;
            case R.id.btn_sleep_detail_share:
                customButtonClickListener.onShareClicked(dataPosition);
                break;
        }
    }

    private void createTimeTexts() {

        int elapsedSec = dataPosition.getElapsedSec();
        int intervalSec;
//        float[] textX = new float[NUMBER_TEXTS];
        ArrayList<Float> textX = new ArrayList<>();
        ArrayList<String> timeTexts = new ArrayList<>();
        if (elapsedSec <= 0.5 * 3600) {
            intervalSec = 10 * 60;
        } else if (elapsedSec <= 1 * 3600) {
            intervalSec = 20 * 60;
        } else if (elapsedSec <= 2 * 3600) {
            intervalSec = 30 * 60;
        } else if (elapsedSec <= 4 * 3600) {
            intervalSec = 3600;
        } else {
            intervalSec = (elapsedSec/3600)/4 * 3600;
        }

        Calendar calendar = Calendar.getInstance();
        int sHour = calendar.get(Calendar.HOUR);
        int sMin = calendar.get(Calendar.MINUTE);
        int sSec = calendar.get(Calendar.SECOND);
        int seconds = sHour * 3600 + sMin * 60 + sSec;
        int offSet = (seconds/intervalSec) * intervalSec - seconds;
        int i = 0;
        while (i < NUMBER_TEXTS) {
            offSet += intervalSec;
            if (offSet >= elapsedSec) break;
            float o = (float) offSet/(float) elapsedSec;
            if (o > 0.9) break;
            if (o < 0.1) continue;
            textX.add(o);
            String timeText;
            if (intervalSec < 3600) {
                timeText = Utils.getDateFromSec(dataPosition.getStartTimeSec() + offSet, "h:mm a");
            } else {
                timeText = Utils.getDateFromSec(dataPosition.getStartTimeSec() + offSet, "h a");
            }
            timeTexts.add(timeText);
        }

        timeLIneView.setTimes(timeTexts, textX);
    }
}