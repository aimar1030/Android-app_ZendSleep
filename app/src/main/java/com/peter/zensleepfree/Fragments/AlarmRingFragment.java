package com.peter.zensleepfree.Fragments;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.peter.zensleepfree.Activity.UpgradeActivity;
import com.peter.zensleepfree.CustomAdapter.AlarmListAdapter;
import com.peter.zensleepfree.CustomAdapter.LockedAlarmListAdapter;
import com.peter.zensleepfree.Interfaces.CustomButtonClickListener;
import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.Constant;
import com.peter.zensleepfree.UtilsClass.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlarmRingFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    public static CustomButtonClickListener customButtonClickListener;
    private ListView listDefaultAlarm, listMyAlarm;
    private Button btnAddMySound;
    private ImageView btnBack, btnSaveSound;

    private AlarmListAdapter alarmListAdapter;
    private LockedAlarmListAdapter lockedAlarmListAdapter;
    private int selectedPosition = -1;
    private MediaPlayer mediaPlayer;

    public AlarmRingFragment() {
        // Required empty public constructor
    }

    public static AlarmRingFragment newInstance(CustomButtonClickListener callback) {
        AlarmRingFragment fragment = new AlarmRingFragment();
        customButtonClickListener = callback;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_alarm_ring, container, false);

        listDefaultAlarm = (ListView) view.findViewById(R.id.list_sound_default);
        listMyAlarm = (ListView) view.findViewById(R.id.list_my_sound);
        btnAddMySound = (Button) view.findViewById(R.id.btn_add_sound);
        btnBack = (ImageView) view.findViewById(R.id.btn_back_to_settings);
        btnSaveSound = (ImageView) view.findViewById(R.id.btn_save_sound);

        alarmListAdapter = new AlarmListAdapter(getActivity());
        lockedAlarmListAdapter = new LockedAlarmListAdapter(getActivity());

        listDefaultAlarm.setAdapter(alarmListAdapter);
        listDefaultAlarm.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listDefaultAlarm.setOnItemClickListener(this);

        listMyAlarm.setAdapter(lockedAlarmListAdapter);

        btnBack.setOnClickListener(this);
        btnAddMySound.setOnClickListener(this);
        btnSaveSound.setOnClickListener(this);

        alarmListAdapter.setSelectedPosition(Utils.getAlarmSoundIndex(getActivity()));
//        alarmListAdapter.notifyDataSetChanged();

        listMyAlarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), UpgradeActivity.class));
            }
        });
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String fileName = Constant.ALARM_SOUNDS[position];
        int resID = getResources().getIdentifier(fileName.toLowerCase(), "raw", getActivity().getPackageName());
        if (position == selectedPosition) {
            mediaPlayer.stop();
        } else {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer = MediaPlayer.create(getActivity(),resID);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        selectedPosition = position;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_sound:
                if (customButtonClickListener != null) {
                    customButtonClickListener.onSaveSoundClicked();
                }
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                String selectedSong = Constant.ALARM_SOUNDS[alarmListAdapter.getSelectedSong()];
                Utils.setAlarmSound(getActivity(), selectedSong);
                Utils.setAlarmSoundIndex(getActivity(), alarmListAdapter.getSelectedSong());
                break;
            case R.id.btn_add_sound:
                startActivity(new Intent(getActivity(), UpgradeActivity.class));
                break;
            case R.id.btn_back_to_settings:
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
                if (customButtonClickListener != null) {
                    customButtonClickListener.onBackToSettingsClicked();
                }
                break;
        }
    }
}
