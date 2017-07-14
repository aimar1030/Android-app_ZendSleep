package com.peter.zensleepfree.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.Constant;
import com.peter.zensleepfree.UtilsClass.KiipHelper;
import com.peter.zensleepfree.UtilsClass.Utils;

import me.kiip.sdk.Kiip;
import me.kiip.sdk.Poptart;

public class ForumActivity extends BaseFragmentActivity {

    private WebView forumWeb;
    private ImageView home, back, forward, refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        forumWeb = (WebView) findViewById(R.id.forumWeb);
        forumWeb.getSettings().setJavaScriptEnabled(true);
        forumWeb.loadUrl("http://forums.zenlabsfitness.com");
        forumWeb.setWebViewClient(new WebViewClient());

        home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                forumWeb.loadUrl("http://forums.zenlabsfitness.com");
            }
        });

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (forumWeb.canGoBack()) {
                    forumWeb.goBack();
                }
            }
        });

        forward = (ImageView) findViewById(R.id.forward);
        forward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (forumWeb.canGoForward()) {
                    forumWeb.goForward();
                }
            }
        });

        refresh = (ImageView) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                forumWeb.reload();
            }
        });

        ImageView backToSettings = (ImageView) findViewById(R.id.imageview_back_to_settings);
        backToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        showKiip();
    }

    private void showKiip() {

//        if(Utils.getBooleanFromSharedPreferences(ForumActivity.this, Constant.SHARED_PREFERENCES_KIIP_REWARDS)){
            Kiip.getInstance().saveMoment(Constant.KIIP_MOMENT_FORUM_VIEWED, new Kiip.Callback() {
                @Override
                public void onFinished(Kiip kiip, Poptart reward) {
                    if (reward == null) {
                        Log.d("kiip_fragment_tag", "Successful moment but no reward to give.");
//                        Toast.makeText(ForumActivity.this, "Kiip: Successful moment but no reward to give.", Toast.LENGTH_LONG).show();
                    } else {
                        showPoptart(reward);
                    }
                }

                @Override
                public void onFailed(Kiip kiip, Exception exception) {
                    Log.d("kiip_fragment_tag", "onFailed ex: " + exception.toString());
                }
            });
//        }
    }

    @Override
    public void onStartSession(KiipHelper helper, Poptart poptart, Exception exception) {
        if (poptart != null) {
            showPoptart(poptart);
        }
        if (exception != null) {
            showError(exception);
        }
    }

    @Override
    public void onEndSession(KiipHelper helper, Exception exception) {

    }

    private void showError(Exception exception) {
        getKiipHelper().showAlert("Kiip Error", exception);
    }
}
