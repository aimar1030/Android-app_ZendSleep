package com.peter.zensleepfree.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.peter.zensleepfree.CustomView.FontTextView;
import com.peter.zensleepfree.Interfaces.CustomButtonClickListener;
import com.peter.zensleepfree.Model.SleepDBModel;
import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.SleepDBHelper;
import com.peter.zensleepfree.UtilsClass.Utils;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;

import at.grabner.circleprogress.CircleProgressView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalyticsFullInsightFragment extends Fragment implements View.OnClickListener {

    private static CustomButtonClickListener customButtonClickListener;

    private ImageView btnDream, imgStatus1, imgStatus2, imgStatus3, imgStatus4, imgStatus5, imgStatus6, imgEffiStatus1, imgEffiStatus2, imgEffiStatus3, imgEffiStatus4, imgEffiStatus5, imgEffiStatus6;
    private FontTextView txtTitle, txtNights, txtAvgSleepDuration, txtSleepedAvg, txtSleepedAvgValue, txtTotalAvg, txtTotalAvgValue, txtSleepedAvgUpdatedValue, txtAvgSleepEffi, txtSelectedAvg, txtSelectedAvgValue, txtTotalAvgEffi, txtTotalAvgProgress;
    private Button btnStatus1, btnStatus2, btnStatus3, btnStatus4, btnStatus5, btnStatus6;
    private CircleProgressView progressEfficiency;

    private ViewGroup transitionsContainer;

    private SleepDBHelper sleepDBHelper;
    private ArrayList<SleepDBModel> sleepDBModels = new ArrayList<>();

//    private boolean isAllUnSelected = true;
    public AnalyticsFullInsightFragment() {
        // Required empty public constructor
    }

    public static AnalyticsFullInsightFragment newInstance(CustomButtonClickListener callBack) {

        AnalyticsFullInsightFragment fragment = new AnalyticsFullInsightFragment();
        customButtonClickListener = callBack;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analytics_full_insight, container, false);

        btnDream                    = (ImageView) view.findViewById(R.id.btn_analytics_dream);
        txtTitle                    = (FontTextView) view.findViewById(R.id.text_analytics_insight_title);
        txtNights                   = (FontTextView) view.findViewById(R.id.text_analytics_insight_nights);
        txtAvgSleepDuration         = (FontTextView) view.findViewById(R.id.text_analytics_insight_avg_sleep_duration);
        txtSleepedAvg               = (FontTextView) view.findViewById(R.id.text_analytics_insight_sleeped_avg);
        txtSleepedAvgValue          = (FontTextView) view.findViewById(R.id.text_analytics_insight_sleeped_avg_value);
        txtTotalAvg                 = (FontTextView) view.findViewById(R.id.text_analytics_insight_total_avg);
        txtTotalAvgValue            = (FontTextView) view.findViewById(R.id.text_analytics_insight_total_avg_value);
        txtSleepedAvgUpdatedValue   = (FontTextView) view.findViewById(R.id.text_analytics_insight_updated_sleeped_avg);
        txtAvgSleepEffi             = (FontTextView) view.findViewById(R.id.text_analytics_insight_avg_efficiency);
        txtSelectedAvg              = (FontTextView) view.findViewById(R.id.text_analytics_insight_sleeped_averg);
        txtSelectedAvgValue         = (FontTextView) view.findViewById(R.id.text_analytics_insight_sleep_updated_efficiency);
        txtTotalAvgEffi             = (FontTextView) view.findViewById(R.id.text_analytics_insight_total_avg_efficiency);
        txtTotalAvgProgress         = (FontTextView) view.findViewById(R.id.text_analytics_insight_total_efficiency_progress_value);
        btnStatus1 = (Button) view.findViewById(R.id.btn_analytics_insight_status_1);
        btnStatus2 = (Button) view.findViewById(R.id.btn_analytics_insight_status_2);
        btnStatus3 = (Button) view.findViewById(R.id.btn_analytics_insight_status_3);
        btnStatus4 = (Button) view.findViewById(R.id.btn_analytics_insight_status_4);
        btnStatus5 = (Button) view.findViewById(R.id.btn_analytics_insight_status_5);
        btnStatus6 = (Button) view.findViewById(R.id.btn_analytics_insight_status_6);
        imgStatus1 = (ImageView) view.findViewById(R.id.image_analytics_insight_status_1);
        imgStatus2 = (ImageView) view.findViewById(R.id.image_analytics_insight_status_2);
        imgStatus3 = (ImageView) view.findViewById(R.id.image_analytics_insight_status_3);
        imgStatus4 = (ImageView) view.findViewById(R.id.image_analytics_insight_status_4);
        imgStatus5 = (ImageView) view.findViewById(R.id.image_analytics_insight_status_5);
        imgStatus6 = (ImageView) view.findViewById(R.id.image_analytics_insight_status_6);
        imgEffiStatus1 = (ImageView) view.findViewById(R.id.image_analytics_insight_efficiency_status_1);
        imgEffiStatus2 = (ImageView) view.findViewById(R.id.image_analytics_insight_efficiency_status_2);
        imgEffiStatus3 = (ImageView) view.findViewById(R.id.image_analytics_insight_efficiency_status_3);
        imgEffiStatus4 = (ImageView) view.findViewById(R.id.image_analytics_insight_efficiency_status_4);
        imgEffiStatus5 = (ImageView) view.findViewById(R.id.image_analytics_insight_efficiency_status_5);
        imgEffiStatus6 = (ImageView) view.findViewById(R.id.image_analytics_insight_efficiency_status_6);
        progressEfficiency = (CircleProgressView) view.findViewById(R.id.progress_analytics_insight_total_avg_efficiency);

        transitionsContainer = (ViewGroup) view.findViewById(R.id.analytics_insight_all_container);

        initUI();
        return view;
    }

    private void initUI() {

        btnDream.setOnClickListener(this);
        btnStatus1.setOnClickListener(this);
        btnStatus2.setOnClickListener(this);
        btnStatus3.setOnClickListener(this);
        btnStatus4.setOnClickListener(this);
        btnStatus5.setOnClickListener(this);
        btnStatus6.setOnClickListener(this);

        sleepDBHelper = new SleepDBHelper(getActivity());
        sleepDBModels = sleepDBHelper.getAllData();

        updateUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_analytics_dream:
                customButtonClickListener.onDreamClicked();
                break;
            case R.id.btn_analytics_insight_status_1:
                btnStatus1.setSelected(!btnStatus1.isSelected());
                break;
            case R.id.btn_analytics_insight_status_2:
                btnStatus2.setSelected(!btnStatus2.isSelected());
                break;
            case R.id.btn_analytics_insight_status_3:
                btnStatus3.setSelected(!btnStatus3.isSelected());
                break;
            case R.id.btn_analytics_insight_status_4:
                btnStatus4.setSelected(!btnStatus4.isSelected());
                break;
            case R.id.btn_analytics_insight_status_5:
                btnStatus5.setSelected(!btnStatus5.isSelected());
                break;
            case R.id.btn_analytics_insight_status_6:
                btnStatus6.setSelected(!btnStatus6.isSelected());
                break;
        }

        updateUI();
    }

    private void updateUI() {
        Button[] statusButtons = { btnStatus1, btnStatus2, btnStatus3, btnStatus4, btnStatus5, btnStatus6 };
        ImageView[] statusImages = { imgStatus1, imgStatus2, imgStatus3, imgStatus4, imgStatus5, imgStatus6 };
        ImageView[] effiImages = { imgEffiStatus1, imgEffiStatus2, imgEffiStatus3, imgEffiStatus4, imgEffiStatus5, imgEffiStatus6 };

        TransitionManager.beginDelayedTransition(transitionsContainer);

        for (int i = 0; i < statusButtons.length; i++) {
            if (statusButtons[i].isSelected()) {
                statusImages[i].setVisibility(View.VISIBLE);
                effiImages[i].setVisibility(View.VISIBLE);
            } else {
                statusImages[i].setVisibility(View.GONE);
                effiImages[i].setVisibility(View.GONE);
            }
        }

        updateAnalyticsData();
    }

    private int getSelectedStatus() {
        Button[] statusButtons = { btnStatus1, btnStatus2, btnStatus3, btnStatus4, btnStatus5, btnStatus6 };
        int status = 0;
        for (int i = 0; i < statusButtons.length; i++) {
            if (statusButtons[i].isSelected()) {
                int s = 1 << i;
                status |= s;
            }
        }
        return status;
    }

    private void updateAnalyticsData() {
        if (sleepDBModels.size() > 0) {
            int status = getSelectedStatus();
            double totalDuration = 0;
            double totalEffi = 0;
            double selectedDuration = 0;
            double selectedEffi = 0;
            int nSelectedSleep = 0;

            for (int i = 0; i < sleepDBModels.size(); i++) {
                double elapsedSec = sleepDBModels.get(i).elapsedSec;
                double efficiency = (sleepDBModels.get(i).getEfficiency() * 100);
                if ((status != 0) && ((sleepDBModels.get(i).status & status) == status)) {
                    selectedDuration += elapsedSec;
                    selectedEffi += efficiency;
                    nSelectedSleep++;
                }
                totalDuration += elapsedSec;
                totalEffi += efficiency;
            }

            double totalAvgDur = totalDuration/(double)sleepDBModels.size();
            double totalAvgEffi = totalEffi/(double)sleepDBModels.size();

            txtNights.setText(String.valueOf(nSelectedSleep) + " / " + String.valueOf(sleepDBModels.size()) + " NIGHTS");
            if (nSelectedSleep > 0) {
                int selectedAvgDur = (int) (selectedDuration/nSelectedSleep);
                double selectedAvgEffi = selectedEffi/(double)nSelectedSleep;
                txtSleepedAvgValue.setText(Utils.getHoursMinutes(selectedAvgDur));

                int diffAvgDur = (int) Math.abs(selectedAvgDur - totalAvgDur);
                String diff = Utils.getHoursMinutes(diffAvgDur);
                if (selectedAvgDur > totalAvgDur) {
                    txtSleepedAvgUpdatedValue.setText("+" + diff);
                } else {
                    txtSleepedAvgUpdatedValue.setText("-" + diff);
                }
                txtSelectedAvgValue.setText(String.valueOf(((int)(selectedAvgEffi - totalAvgEffi)*10)/10.0) + "%");
                progressEfficiency.setValue((float) selectedAvgEffi);
                txtTotalAvgProgress.setText(String.valueOf((int)selectedAvgEffi) + "%");
            } else {
                txtSleepedAvgValue.setText("0h 0min");
                txtSleepedAvgUpdatedValue.setText("0h 0min");
                txtSelectedAvgValue.setText("0.0%");
                progressEfficiency.setValue((float) totalAvgEffi);
            }
            txtTotalAvgValue.setText(Utils.getHoursMinutes((int) (totalDuration/sleepDBModels.size())));
            progressEfficiency.setValue((float) totalAvgEffi);
            txtTotalAvgProgress.setText(String.valueOf((int)totalAvgEffi) + "%");
        } else {
            txtNights.setText("");
            progressEfficiency.setValue(0);
            txtTotalAvgProgress.setText("0%");
            txtSelectedAvgValue.setText("");
            txtTotalAvgValue.setText("");
        }
    }
}
