package com.peter.zensleepfree.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.FrameLayout;

import com.peter.zensleepfree.Fragments.AlarmRingFragment;
import com.peter.zensleepfree.Fragments.AlarmSettingFragment;
import com.peter.zensleepfree.Fragments.AnalyticsFragment;
import com.peter.zensleepfree.Fragments.AnalyticsFullFragment;
import com.peter.zensleepfree.Fragments.AnalyticsFullInsightFragment;
import com.peter.zensleepfree.Fragments.NotesFragment;
import com.peter.zensleepfree.Fragments.SettingsFragment;
import com.peter.zensleepfree.Fragments.ShareFragment;
import com.peter.zensleepfree.Fragments.SleepDetailFragment;
import com.peter.zensleepfree.Fragments.SleepFragment;
import com.peter.zensleepfree.Fragments.StatsFragment;
import com.peter.zensleepfree.Interfaces.CustomButtonClickListener;
import com.peter.zensleepfree.Model.SleepDBModel;
import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.Constant;
import com.peter.zensleepfree.UtilsClass.KiipHelper;
import com.peter.zensleepfree.UtilsClass.Utils;
import com.peter.zensleepfree.UtilsClass.ZenSleep;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import me.kiip.sdk.Kiip;
import me.kiip.sdk.Poptart;

public class TabActivity extends BaseFragmentActivity implements OnMenuTabClickListener, CustomButtonClickListener {

    private static final String TAG = "Tab Activity";
    private static final int REQUEST_CODE_RESOLUTION = 1;
    private BottomBar bottomBar;
    private FrameLayout fragmentContainer;
    private CustomButtonClickListener sleepCallback = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        bottomBar = BottomBar.attach(this, savedInstanceState);
        bottomBar.useFixedMode();
        bottomBar.setItems(R.menu.tab_menu);

        bottomBar.setOnMenuTabClickListener(this);

        fragmentContainer = (FrameLayout) this.findViewById(R.id.fragment_container);

        Fragment fragment = SleepFragment.newInstance(sleepCallback);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        bottomBar.setTextAppearance(R.style.TabText);
        bottomBar.setTypeFace("Gotham-Medium.ttf");
        bottomBar.setActiveTabColor(Color.parseColor("#eca813"));
        bottomBar.useDarkTheme();
    }

    @Override
    public void onMenuTabSelected(@IdRes int menuItemId) {
        Fragment fragment;
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        switch (menuItemId) {
            case R.id.tab_sleep:
                fragment = SleepFragment.newInstance(sleepCallback);
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
                break;
            case R.id.tab_notes:
                fragment = NotesFragment.newInstance(sleepCallback);
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
                showKiip();
                break;
            case R.id.tab_analytics:
                fragment = AnalyticsFragment.newInstance();
                fm = getSupportFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
                break;
            case R.id.tab_stats:
                fragment = StatsFragment.newInstance();
                fm = getSupportFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
                break;
            case R.id.tab_settings:
                fragment = SettingsFragment.newInstance();
                fm = getSupportFragmentManager();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
                break;
        }
    }

    @Override
    public void onMenuTabReSelected(@IdRes int menuItemId) {

    }

    @Override
    public void onAlarmClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        Fragment fragment = AlarmSettingFragment.newInstance(sleepCallback);
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onStartSleep() {
        if (!Utils.getHidePrepareSleep(this)) {
            Intent intent = new Intent(this, PrepareSleepActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, SleepActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onSaveButtonClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        Fragment fragment = SleepFragment.newInstance(sleepCallback);
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSaveSoundClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        Fragment fragment = AlarmSettingFragment.newInstance(sleepCallback);
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onAlarmSoundClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        Fragment fragment = AlarmRingFragment.newInstance(sleepCallback);
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        Fragment fragment = SleepFragment.newInstance(sleepCallback);
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackToSettingsClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        Fragment fragment = AlarmSettingFragment.newInstance(sleepCallback);
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onSleepDataClicked(SleepDBModel data) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        Fragment fragment = SleepDetailFragment.newInstance(sleepCallback, data);
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackToNotesClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        Fragment fragment = NotesFragment.newInstance(sleepCallback);
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onShareClicked(SleepDBModel data) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        Fragment fragment = ShareFragment.newInstance(sleepCallback, data);
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onInsightClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        Fragment fragment = AnalyticsFullInsightFragment.newInstance(sleepCallback);
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onDreamClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        Fragment fragment = AnalyticsFullFragment.newInstance(sleepCallback);
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFinishShareClicked(SleepDBModel data) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction;
        Fragment fragment = SleepDetailFragment.newInstance(sleepCallback, data);
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ZenSleep.sleepFinished) {
            bottomBar.selectTabAtPosition(1, false);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction;
            Fragment fragment = SleepDetailFragment.newInstance(sleepCallback, ZenSleep.tempModel);
            fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void showKiip() {

//        if(Utils.getBooleanFromSharedPreferences(TabActivity.this, Constant.SHARED_PREFERENCES_KIIP_REWARDS)){
            Kiip.getInstance().saveMoment(Constant.KIIP_MOMENT_NOTE_VIEWED, new Kiip.Callback() {
                @Override
                public void onFinished(Kiip kiip, Poptart reward) {
                    if (reward == null) {
                        Log.d("kiip_fragment_tag", "Successful moment but no reward to give.");
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
