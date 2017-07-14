package com.peter.zensleepfree.Fragments.Intros;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.peter.zensleepfree.Interfaces.IntroPageButtonListener;
import com.peter.zensleepfree.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class Intro3Fragment extends Fragment implements View.OnClickListener {

    private int[] images = { R.drawable.intro3_00000, R.drawable.intro3_00001, R.drawable.intro3_00002, R.drawable.intro3_00003, R.drawable.intro3_00004,
            R.drawable.intro3_00005, R.drawable.intro3_00006, R.drawable.intro3_00007, R.drawable.intro3_00008, R.drawable.intro3_00009,
            R.drawable.intro3_00010, R.drawable.intro3_00011, R.drawable.intro3_00012, R.drawable.intro3_00013, R.drawable.intro3_00014,
            R.drawable.intro3_00015, R.drawable.intro3_00016, R.drawable.intro3_00017, R.drawable.intro3_00018, R.drawable.intro3_00019,
            R.drawable.intro3_00020, R.drawable.intro3_00021, R.drawable.intro3_00022, R.drawable.intro3_00023, R.drawable.intro3_00024,
            R.drawable.intro3_00025, R.drawable.intro3_00026, R.drawable.intro3_00027, R.drawable.intro3_00028, R.drawable.intro3_00029,
            R.drawable.intro3_00030, R.drawable.intro3_00031, R.drawable.intro3_00032, R.drawable.intro3_00033, R.drawable.intro3_00034,
            R.drawable.intro3_00035, R.drawable.intro3_00036, R.drawable.intro3_00037, R.drawable.intro3_00038, R.drawable.intro3_00039,
            R.drawable.intro3_00040, R.drawable.intro3_00041, R.drawable.intro3_00042, R.drawable.intro3_00043, R.drawable.intro3_00044,
            R.drawable.intro3_00045, R.drawable.intro3_00046, R.drawable.intro3_00047, R.drawable.intro3_00048, R.drawable.intro3_00049,
            R.drawable.intro3_00050, R.drawable.intro3_00051, R.drawable.intro3_00052, R.drawable.intro3_00053, R.drawable.intro3_00054,
            R.drawable.intro3_00055, R.drawable.intro3_00056, R.drawable.intro3_00057, R.drawable.intro3_00058, R.drawable.intro3_00059,
            R.drawable.intro3_00060, R.drawable.intro3_00061, R.drawable.intro3_00062, R.drawable.intro3_00063 };

    private Timer animationTimer = new Timer();
    private ImageView animeImageView;
    private Button btnClose;
    private int imageIndex = 0;

    public static IntroPageButtonListener buttonListener;

    public Intro3Fragment() {
        // Required empty public constructor
    }
    public static Intro3Fragment newInstance(IntroPageButtonListener callBack) {
        Intro3Fragment fragment = new Intro3Fragment();
        buttonListener = callBack;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro3, container, false);
        animeImageView = (ImageView) view.findViewById(R.id.intro_3_imageview);
        btnClose = (Button) view.findViewById(R.id.btn_intro_3_close);
        btnClose.setOnClickListener(this);
        return view;
    }

    public void onPageSelected() {
        animationTimer = new Timer();
        imageIndex = 0;
        animationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshImage();
            }
        }, 0, 100);
    }

    public void onPageUnSelected() {
        animationTimer.cancel();
    }

    private void refreshImage() {

        if (getActivity() == null) {
            return ;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (imageIndex < images.length) {
                    animeImageView.setImageResource(images[imageIndex]);
                    imageIndex++;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        buttonListener.onCloseClicked();
    }
}
