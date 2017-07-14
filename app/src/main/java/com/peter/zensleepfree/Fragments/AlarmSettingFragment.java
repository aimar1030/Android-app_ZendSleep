package com.peter.zensleepfree.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;

import com.peter.zensleepfree.Activity.UpgradeActivity;
import com.peter.zensleepfree.CustomView.FontTextView;
import com.peter.zensleepfree.Interfaces.CustomButtonClickListener;
import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.Constant;
import com.peter.zensleepfree.UtilsClass.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmSettingFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private RelativeLayout layoutSmart, layoutSnooze, layoutSound;
    private FontTextView txtSelectedSound, txtSmartAlarmValue, txtSmartAlarmMax, txtSnoozeValue, txtSnoozeMax;
    private Switch chkAlarm, chkSmart, chkSnooze;
    private ImageView btnSave, btnBack, imgVolumeUp, imgVolumeDown, txtSmartAlarmMin, txtSnoozeMin;
    private SeekBar seekVolume, seekSmart, seekSnooze;

    private int ACTION_ALARM = 0;
    private int ACTION_SMART = 1;
    private int ACTION_SNOOZING = 2;

    public static CustomButtonClickListener customButtonClickListener;

    public AlarmSettingFragment() {
        // Required empty public constructor
    }

    public static AlarmSettingFragment newInstance(CustomButtonClickListener callBack) {

        AlarmSettingFragment fragment = new AlarmSettingFragment();
        customButtonClickListener = callBack;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alarm_setting, container, false);

        layoutSmart = (RelativeLayout) view.findViewById(R.id.layout_smart);
        layoutSnooze = (RelativeLayout) view.findViewById(R.id.layout_snooze);
        layoutSound = (RelativeLayout) view.findViewById(R.id.layout_sound);
        txtSelectedSound = (FontTextView) view.findViewById(R.id.text_selected_sound);
        txtSmartAlarmValue = (FontTextView) view.findViewById(R.id.text_smart_alarm_value);
        txtSmartAlarmMin = (ImageView) view.findViewById(R.id.text_smart_alarm_min);
        txtSmartAlarmMax = (FontTextView) view.findViewById(R.id.text_smart_alarm_max);
        txtSnoozeValue = (FontTextView) view.findViewById(R.id.text_snooze_alarm_value);
        txtSnoozeMin = (ImageView) view.findViewById(R.id.text_snooze_alarm_min);
        txtSnoozeMax = (FontTextView) view.findViewById(R.id.text_snooze_alarm_max);
        chkAlarm = (Switch) view.findViewById(R.id.switch_alarm);
        chkSmart = (Switch) view.findViewById(R.id.switch_smart);
        chkSnooze = (Switch) view.findViewById(R.id.switch_snoozing);
        btnSave = (ImageView) view.findViewById(R.id.btn_save);
        btnBack = (ImageView) view.findViewById(R.id.btn_back);
        seekSmart = (SeekBar) view.findViewById(R.id.seek_smart_alarm);
        seekSnooze = (SeekBar) view.findViewById(R.id.seek_snoozing);
        seekVolume = (SeekBar) view.findViewById(R.id.seek_volume);
        imgVolumeDown = (ImageView) view.findViewById(R.id.image_volume_down);
        imgVolumeUp = (ImageView) view.findViewById(R.id.image_volume_up);

        btnSave.setOnClickListener(this);
        layoutSound.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        seekVolume.setOnSeekBarChangeListener(this);

        seekSmart.setEnabled(false);
        seekSnooze.setEnabled(false);

        layoutSmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UpgradeActivity.class));
            }
        });

        layoutSnooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UpgradeActivity.class));
            }
        });

        chkAlarm.setOnCheckedChangeListener(this);
        chkSmart.setOnCheckedChangeListener(this);
        chkSnooze.setOnCheckedChangeListener(this);

        initUI();

        return view;
    }

    private void initUI () {
        chkAlarm.setChecked(Utils.getEnableAlarm(getActivity()));
        chkSmart.setChecked(Utils.getEnableSmartAlarm(getActivity()));
        chkSnooze.setChecked(Utils.getEnableSnooze(getActivity()));

        seekVolume.setProgress(Utils.getAlarmVolume(getActivity()));
        seekSmart.setProgress(Utils.getSmartAlarmCoolTime(getActivity()) - 5);
        seekSnooze.setProgress(Utils.getSnoozeCoolTime(getActivity()) - 1);

        txtSmartAlarmValue.setText(String.valueOf(Utils.getSmartAlarmCoolTime(getActivity())) + "min");
        txtSnoozeValue.setText(String.valueOf(Utils.getSnoozeCoolTime(getActivity())) + "min");

        if (Utils.getAlarmSound(getActivity()).equals("")) {
            txtSelectedSound.setText(Constant.ALARM_SOUNDS[0]);
        } else {
            txtSelectedSound.setText(Utils.getAlarmSound(getActivity()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                if (customButtonClickListener != null) {
                    customButtonClickListener.onSaveButtonClicked();
                }
                break;
            case R.id.layout_sound:
                if (customButtonClickListener != null) {
                    customButtonClickListener.onAlarmSoundClicked();
                }
                break;
            case R.id.btn_back:
                if (customButtonClickListener != null) {
                    customButtonClickListener.onBackClicked();
                }
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seek_smart_alarm:
                startActivity(new Intent(getActivity(), UpgradeActivity.class));
//                Utils.setSmartAlarmCoolTime(getActivity(), progress + 5);
                break;
            case R.id.seek_snoozing:
                startActivity(new Intent(getActivity(), UpgradeActivity.class));
//                Utils.setSnoozeCoolTime(getActivity(), progress + 1);
                break;
            case R.id.seek_volume:
                Utils.setAlarmVolume(getActivity(), progress);
                break;
        }
        updateUI();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_alarm:
                Utils.setEnableAlarm(getActivity(), isChecked);
                break;
            case R.id.switch_smart:
                Utils.setEnableSmartAlarm(getActivity(), isChecked);
                break;
            case R.id.switch_snoozing:
                Utils.setEnableSnooze(getActivity(), isChecked);
                break;
        }
        updateUI();
    }

    private void updateUI() {
        if (Utils.getEnableAlarm(getActivity())) {
            layoutSound.setVisibility(View.VISIBLE);
            layoutSmart.setVisibility(View.VISIBLE);
            layoutSnooze.setVisibility(View.VISIBLE);
            seekVolume.setVisibility(View.VISIBLE);
            imgVolumeDown.setVisibility(View.VISIBLE);
            imgVolumeUp.setVisibility(View.VISIBLE);
        } else {
            layoutSound.setVisibility(View.GONE);
            layoutSmart.setVisibility(View.GONE);
            layoutSnooze.setVisibility(View.GONE);
            seekVolume.setVisibility(View.GONE);
            imgVolumeDown.setVisibility(View.GONE);
            imgVolumeUp.setVisibility(View.GONE);
        }

        if (Utils.getEnableSmartAlarm(getActivity())) {
            seekSmart.setVisibility(View.VISIBLE);
            txtSmartAlarmValue.setVisibility(View.VISIBLE);
            txtSmartAlarmMin.setVisibility(View.VISIBLE);
            txtSmartAlarmMax.setVisibility(View.VISIBLE);
        } else {
            seekSmart.setVisibility(View.GONE);
            txtSmartAlarmValue.setVisibility(View.GONE);
            txtSmartAlarmMin.setVisibility(View.GONE);
            txtSmartAlarmMax.setVisibility(View.GONE);
        }

        if (Utils.getEnableSnooze(getActivity())) {
            seekSnooze.setVisibility(View.VISIBLE);
            txtSnoozeMax.setVisibility(View.VISIBLE);
            txtSnoozeMin.setVisibility(View.VISIBLE);
            txtSnoozeValue.setVisibility(View.VISIBLE);
        } else {
            seekSnooze.setVisibility(View.GONE);
            txtSnoozeMax.setVisibility(View.GONE);
            txtSnoozeMin.setVisibility(View.GONE);
            txtSnoozeValue.setVisibility(View.GONE);
        }
        txtSmartAlarmValue.setText(String.valueOf(Utils.getSmartAlarmCoolTime(getActivity())) + "min");
        txtSnoozeValue.setText(String.valueOf(Utils.getSnoozeCoolTime(getActivity())) + "min");
    }
}
