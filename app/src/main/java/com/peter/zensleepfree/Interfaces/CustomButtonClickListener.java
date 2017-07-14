package com.peter.zensleepfree.Interfaces;

import com.peter.zensleepfree.Model.SleepDBModel;

/**
 * Created by peter on 6/20/16.
 */
public interface CustomButtonClickListener {
    public void onAlarmClicked();
    public void onStartSleep();
    public void onSaveButtonClicked();
    public void onSaveSoundClicked();
    public void onAlarmSoundClicked();
    public void onBackClicked();
    public void onBackToSettingsClicked();
    public void onSleepDataClicked(SleepDBModel position);
    public void onBackToNotesClicked();
    public void onShareClicked(SleepDBModel data);
    public void onFinishShareClicked(SleepDBModel data);
    public void onInsightClicked();
    public void onDreamClicked();
}