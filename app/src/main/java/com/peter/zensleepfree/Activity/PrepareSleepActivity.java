package com.peter.zensleepfree.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.peter.zensleepfree.CustomView.FontTextView;
import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.Utils;

public class PrepareSleepActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private FontTextView titleText, txtPlaceContent;
    private Button btnNext;
    private CheckBox chkDoNotShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_sleep);

        titleText = (FontTextView) findViewById(R.id.text_place_phone);
        txtPlaceContent = (FontTextView) findViewById(R.id.text_place_phone_content);
        btnNext = (Button) findViewById(R.id.btn_place_phone_next);
        chkDoNotShow = (CheckBox) findViewById(R.id.checkBox_do_not_show);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Gotham-Light.ttf");

        btnNext.setTypeface(typeface);
        chkDoNotShow.setTypeface(typeface);

        btnNext.setOnClickListener(this);
        chkDoNotShow.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, SleepActivity.class);
//        Intent intent = new Intent(this, AudioSignalActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Utils.setHidePrepareSleep(this, isChecked);
    }
}
