package com.peter.zensleepfree.Activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.Constant;
import com.peter.zensleepfree.UtilsClass.ZenSleep;

public class DreamActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btnBack, btnSave, btnPleasant, btnNeutral, btnBad, imgCheckPleasant, imgCheckNeutral, imgCheckBad;
    private EditText edtDreamNote;
    private int dreamResult = Constant.SLEEP_DREAM_NONE;
    private String dreamNote = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream);

        btnSave = (ImageView) findViewById(R.id.btn_dream_detail_save);
        btnBack = (ImageView) findViewById(R.id.btn_dream_detail_back);
        btnPleasant = (ImageView) findViewById(R.id.btn_dream_pleasant);
        btnNeutral = (ImageView) findViewById(R.id.btn_dream_neutral);
        btnBad = (ImageView) findViewById(R.id.btn_dream_bad);
        imgCheckPleasant = (ImageView) findViewById(R.id.btn_dream_check_pleasant);
        imgCheckNeutral = (ImageView) findViewById(R.id.btn_dream_check_neutral);
        imgCheckBad = (ImageView) findViewById(R.id.btn_dream_check_bad);
        edtDreamNote = (EditText) findViewById(R.id.edit_dream_note);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Gotham-Light.ttf");
        edtDreamNote.setTypeface(typeface);

        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnBad.setOnClickListener(this);
        btnNeutral.setOnClickListener(this);
        btnPleasant.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dream_detail_back:
                finish();
                break;
            case R.id.btn_dream_detail_save:
                dreamNote = edtDreamNote.getText().toString();
                ZenSleep.tempModel.setDream(dreamResult);
                ZenSleep.tempModel.setNote(dreamNote);
                finish();
                break;
            case R.id.btn_dream_pleasant:
                imgCheckPleasant.setAlpha(1.0f);
                imgCheckNeutral.setAlpha(0.0f);
                imgCheckBad.setAlpha(0.0f);
                dreamResult = Constant.SLEEP_DREAM_PLEASANT;
                break;
            case R.id.btn_dream_neutral:
                imgCheckPleasant.setAlpha(0.0f);
                imgCheckNeutral.setAlpha(1.0f);
                imgCheckBad.setAlpha(0.0f);
                dreamResult = Constant.SLEEP_DREAM_NEUTRAL;
                break;
            case R.id.btn_dream_bad:
                imgCheckPleasant.setAlpha(0.0f);
                imgCheckNeutral.setAlpha(0.0f);
                imgCheckBad.setAlpha(1.0f);
                dreamResult = Constant.SLEEP_DREAM_BAD;
                break;
        }
    }
}
