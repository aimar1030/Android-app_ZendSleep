package com.peter.zensleepfree.Activity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.peter.zensleepfree.R;

public class UpgradeActivity extends AppCompatActivity {

    private ImageView btnClose;
    private Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);

        btnClose = (ImageView) findViewById(R.id.btn_upgrade_close);
        btnConfirm = (Button) findViewById(R.id.btn_upgrade_confirm);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Gotham-Book.ttf");
        btnConfirm.setTypeface(typeface);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
