package com.peter.zensleepfree.Activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.Constant;
import com.peter.zensleepfree.UtilsClass.SleepDBHelper;
import com.peter.zensleepfree.UtilsClass.Utils;
import com.peter.zensleepfree.UtilsClass.ZenSleep;

import at.grabner.circleprogress.CircleProgressView;

public class SleepResultActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private TextView titleText, txtFeel, txtDream, txtPleasant, txtNeutral, txtBad, txtEfficienty;
    private ImageView btnSave, btnPleasant, btnNeutral, btnBad, imgCheckPleasant, imgCheckNeutral, imgCheckBad;
    private EditText edtDreamNote;
    private CircleProgressView efficientyView;
    private RadioGroup sleepResult;

    private int mood, dreamResult;
    private String dreamNote;
    private SleepDBHelper sleepDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_result);

        titleText = (TextView) this.findViewById(R.id.result_title);
        txtFeel = (TextView) this.findViewById(R.id.text_feel);
        txtDream = (TextView) this.findViewById(R.id.text_result_dream);
        txtPleasant = (TextView) findViewById(R.id.text_result_pleasant);
        txtNeutral = (TextView) findViewById(R.id.text_result_neutral);
        txtBad = (TextView) findViewById(R.id.text_result_bad);
        txtEfficienty = (TextView) findViewById(R.id.text_result_efficienty);
        btnSave = (ImageView) findViewById(R.id.btn_save_result);
        btnPleasant = (ImageView) findViewById(R.id.btn_result_pleasant);
        btnNeutral = (ImageView) findViewById(R.id.btn_result_neutral);
        btnBad = (ImageView) findViewById(R.id.btn_result_bad);
        imgCheckPleasant = (ImageView) findViewById(R.id.btn_check_pleasant);
        imgCheckNeutral = (ImageView) findViewById(R.id.btn_check_neutral);
        imgCheckBad = (ImageView) findViewById(R.id.btn_check_bad);
        edtDreamNote = (EditText) findViewById(R.id.edit_result_note);
        efficientyView = (CircleProgressView) findViewById(R.id.result_efficienty);
        sleepResult = (RadioGroup) findViewById(R.id.radio_result);

        sleepResult.setOnCheckedChangeListener(this);
        btnSave.setOnClickListener(this);
        btnBad.setOnClickListener(this);
        btnNeutral.setOnClickListener(this);
        btnPleasant.setOnClickListener(this);

        initUI();
    }

    private void initUI () {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Gotham-Light.ttf");
        titleText.setTypeface(typeface);
        txtFeel.setTypeface(typeface);
        txtDream.setTypeface(typeface);
        txtPleasant.setTypeface(typeface);
        txtNeutral.setTypeface(typeface);
        txtBad.setTypeface(typeface);
        efficientyView.setTextTypeface(typeface);
        edtDreamNote.setTypeface(typeface);
        txtEfficienty.setTypeface(typeface);
//        efficientyView.setValue(35);

        mood = -1;

        sleepDBHelper = new SleepDBHelper(this);
        efficientyView.setValue(ZenSleep.tempModel.getEfficiency() * 100);
        int efficienty = (int) (ZenSleep.tempModel.getEfficiency() * 10000);
        txtEfficienty.setText(String.valueOf(efficienty/100) + "%");

        dreamResult = ZenSleep.tempModel.getDream();
        dreamNote = ZenSleep.tempModel.getNote();
        edtDreamNote.setText(dreamNote);
        if (dreamResult == Constant.SLEEP_DREAM_PLEASANT) {
            imgCheckPleasant.setAlpha(1.0f);
            imgCheckNeutral.setAlpha(0.0f);
            imgCheckBad.setAlpha(0.0f);
        } else if (dreamResult == Constant.SLEEP_DREAM_NEUTRAL) {
            imgCheckPleasant.setAlpha(0.0f);
            imgCheckNeutral.setAlpha(1.0f);
            imgCheckBad.setAlpha(0.0f);
        } else if (dreamResult == Constant.SLEEP_DREAM_BAD) {
            imgCheckPleasant.setAlpha(0.0f);
            imgCheckNeutral.setAlpha(0.0f);
            imgCheckBad.setAlpha(1.0f);
        } else {
            imgCheckPleasant.setAlpha(0.0f);
            imgCheckNeutral.setAlpha(0.0f);
            imgCheckBad.setAlpha(0.0f);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.radioButton:
                Log.d("Checked", String.valueOf(checkedId));
                mood = Constant.SLEEP_MOOD_5;
                break;
            case R.id.radioButton2:
                mood = Constant.SLEEP_MOOD_4;
                break;
            case R.id.radioButton3:
                mood = Constant.SLEEP_MOOD_3;
                break;
            case R.id.radioButton4:
                mood = Constant.SLEEP_MOOD_2;
                break;
            case R.id.radioButton5:
                mood = Constant.SLEEP_MOOD_1;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_result:
                dreamNote = edtDreamNote.getText().toString();
                saveData();
                finish();
                break;
            case R.id.btn_result_pleasant:
                imgCheckPleasant.setAlpha(1.0f);
                imgCheckNeutral.setAlpha(0.0f);
                imgCheckBad.setAlpha(0.0f);
                dreamResult = Constant.SLEEP_DREAM_PLEASANT;
                break;
            case R.id.btn_result_neutral:
                imgCheckPleasant.setAlpha(0.0f);
                imgCheckNeutral.setAlpha(1.0f);
                imgCheckBad.setAlpha(0.0f);
                dreamResult = Constant.SLEEP_DREAM_NEUTRAL;
                break;
            case R.id.btn_result_bad:
                imgCheckPleasant.setAlpha(0.0f);
                imgCheckNeutral.setAlpha(0.0f);
                imgCheckBad.setAlpha(1.0f);
                dreamResult = Constant.SLEEP_DREAM_BAD;
                break;
        }
    }

    private void saveData() {
        ZenSleep.tempModel.setMood(mood);
        ZenSleep.tempModel.setDream(dreamResult);
        ZenSleep.tempModel.setNote(dreamNote);
        sleepDBHelper.addSleepData(ZenSleep.tempModel);
        Utils.writeJSON(ZenSleep.tempModel);
//        GoogleDriveClient.getDriveClient(this).uploadFile(ZenSleep.tempModel);
        ZenSleep.sleepFinished = true;
    }
}
