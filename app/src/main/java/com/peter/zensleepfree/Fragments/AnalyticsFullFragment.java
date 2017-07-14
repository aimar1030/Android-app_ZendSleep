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
import com.peter.zensleepfree.UtilsClass.Constant;
import com.peter.zensleepfree.UtilsClass.SleepDBHelper;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalyticsFullFragment extends Fragment implements View.OnClickListener {

    private ImageView btnInsight, imgPleasant, imgNeutral, imgBad;
    private FontTextView txtTitle, txtPleasantProgress, txtNeutralProgress, txtBadProgress;
    private Button btnWorkedOut, btnStress, btnNotMyBed, btnCaffeine, btnMissedMeal, btnAlcohol;

    private static CustomButtonClickListener customButtonClickListener;

    private SleepDBHelper sleepDBHelper;
    private ArrayList<SleepDBModel> sleepDBModels = new ArrayList<>();

    private int pPleasant, pNeutral, pBad;
    private int maxHeight = 0;
    private ViewGroup transitionsContainer;

    public AnalyticsFullFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance(CustomButtonClickListener callBack) {
        AnalyticsFullFragment fragment = new AnalyticsFullFragment();
        customButtonClickListener = callBack;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analytics_full, container, false);

        btnInsight = (ImageView) view.findViewById(R.id.btn_analytics_insight);
        imgPleasant = (ImageView) view.findViewById(R.id.image_analytics_pleasant);
        imgNeutral = (ImageView) view.findViewById(R.id.image_analytics_neutral);
        imgBad = (ImageView) view.findViewById(R.id.image_analytics_bad);
        txtTitle = (FontTextView) view.findViewById(R.id.text_analytics_full_title);
        txtPleasantProgress = (FontTextView) view.findViewById(R.id.text_analytics_pleasant_progress);
        txtNeutralProgress = (FontTextView) view.findViewById(R.id.text_analytics_neutral_progress);
        txtBadProgress = (FontTextView) view.findViewById(R.id.text_analytics_bad_progress);
        btnWorkedOut = (Button) view.findViewById(R.id.btn_analytics_worked_out);
        btnStress = (Button) view.findViewById(R.id.btn_analytics_stress);
        btnNotMyBed = (Button) view.findViewById(R.id.btn_analytics_not_my_bed);
        btnCaffeine = (Button) view.findViewById(R.id.btn_analytics_caffeine);
        btnMissedMeal = (Button) view.findViewById(R.id.btn_analytics_missed_meal);
        btnAlcohol = (Button) view.findViewById(R.id.btn_analytics_alcohol);

        transitionsContainer = (ViewGroup) view.findViewById(R.id.analytics_insight_full_dream_container);

        initUI();

        return view;
    }

    private void initUI() {
        btnAlcohol.setOnClickListener(this);
        btnMissedMeal.setOnClickListener(this);
        btnCaffeine.setOnClickListener(this);
        btnNotMyBed.setOnClickListener(this);
        btnInsight.setOnClickListener(this);
        btnStress.setOnClickListener(this);
        btnWorkedOut.setOnClickListener(this);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) txtPleasantProgress.getLayoutParams();
        maxHeight = params.height;

        sleepDBHelper = new SleepDBHelper(getActivity());
        sleepDBModels = sleepDBHelper.getAllData();

        updateDiagram();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_analytics_alcohol:
                btnAlcohol.setSelected(!btnAlcohol.isSelected());
                break;
            case R.id.btn_analytics_worked_out:
                btnWorkedOut.setSelected(!btnWorkedOut.isSelected());
                break;
            case R.id.btn_analytics_stress:
                btnStress.setSelected(!btnStress.isSelected());
                break;
            case R.id.btn_analytics_not_my_bed:
                btnNotMyBed.setSelected(!btnNotMyBed.isSelected());
                break;
            case R.id.btn_analytics_caffeine:
                btnCaffeine.setSelected(!btnCaffeine.isSelected());
                break;
            case R.id.btn_analytics_missed_meal:
                btnMissedMeal.setSelected(!btnMissedMeal.isSelected());
                break;
            case R.id.btn_analytics_insight:
                customButtonClickListener.onInsightClicked();
                break;
        }
        updateDiagram();
    }

    private void updateDiagram() {
        Button[] statusButtons = { btnWorkedOut, btnStress, btnNotMyBed, btnCaffeine, btnMissedMeal, btnAlcohol };
        int status = 0;

        for (int i = 0; i < statusButtons.length; i++) {
            if (statusButtons[i].isSelected()) {
                int s = 1 << i;
                status |= s;
            }
        }
        ArrayList<SleepDBModel> dreamSleeps = new ArrayList<>();
        int dreamNights = 0;

        for (int i = 0; i < sleepDBModels.size(); i++) {
            SleepDBModel sleepDBModel = sleepDBModels.get(i);
            if ((status == 0 || (sleepDBModel.status & status) == status) && (sleepDBModel.dream != Constant.SLEEP_DREAM_NONE)) {
                dreamNights++;
                dreamSleeps.add(sleepDBModel);
            }
        }
        if (dreamNights > 0) {
            int p = (int) ((double)dreamNights/(double)sleepDBModels.size() * 100);
            txtTitle.setText("Your dream in " + String.valueOf(p) + "% of nights");
            int nPleasant = 0;
            int nNeutral = 0;
            int nBad = 0;

            for (int i = 0; i < dreamSleeps.size(); i++) {
                if (dreamSleeps.get(i).dream == Constant.SLEEP_DREAM_PLEASANT)
                    nPleasant++;
                else if (dreamSleeps.get(i).dream == Constant.SLEEP_DREAM_PLEASANT)
                    nNeutral++;
                else
                    nBad++;
            }
            pPleasant = (int) ((double)nPleasant/(double)dreamNights*100);
            pNeutral = (int) ((double)nNeutral/(double)dreamNights*100);
            pBad = 0;
            if( nBad == 0 )
                pNeutral = 100 - pPleasant;
            else
                pBad = 100 - pPleasant - pNeutral;
        } else {
            txtTitle.setText("Your dream in 0% of nights");
            pPleasant = 0;
            pNeutral = 0;
            pBad = 0;
        }

        updateUI();
    }

    private void updateUI() {
        ViewGroup.MarginLayoutParams paramsPleasant = (ViewGroup.MarginLayoutParams) txtPleasantProgress.getLayoutParams();
        ViewGroup.MarginLayoutParams paramsNeutral = (ViewGroup.MarginLayoutParams) txtNeutralProgress.getLayoutParams();
        ViewGroup.MarginLayoutParams paramsBad = (ViewGroup.MarginLayoutParams) txtBadProgress.getLayoutParams();
        TransitionManager.beginDelayedTransition(transitionsContainer);
        if (pPleasant == 0){
            txtPleasantProgress.setVisibility(View.GONE);
        } else {
            txtPleasantProgress.setVisibility(View.VISIBLE);
            paramsPleasant.height = maxHeight * pPleasant / 100;
            txtPleasantProgress.setLayoutParams(paramsPleasant);
            txtPleasantProgress.setText(String.valueOf(pPleasant) + "%");
        }
        if (pNeutral == 0) {
            txtNeutralProgress.setVisibility(View.GONE);
        } else {
            txtNeutralProgress.setVisibility(View.VISIBLE);
            paramsNeutral.height = maxHeight * pNeutral/100;
            txtNeutralProgress.setLayoutParams(paramsNeutral);
            txtNeutralProgress.setText(String.valueOf(pNeutral) + "%");
        }
        if (pBad == 0) {
            txtBadProgress.setVisibility(View.GONE);
        } else {
            txtBadProgress.setVisibility(View.VISIBLE);
            paramsBad.height = maxHeight * pBad/100;
            txtBadProgress.setLayoutParams(paramsBad);
            txtBadProgress.setText(String.valueOf(pBad) + "%");
        }
    }
}
