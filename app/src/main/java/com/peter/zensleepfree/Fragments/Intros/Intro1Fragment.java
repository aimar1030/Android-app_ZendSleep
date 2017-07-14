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
public class Intro1Fragment extends Fragment implements View.OnClickListener {

    private int[] images = { R.drawable.intro1_00001, R.drawable.intro1_00001, R.drawable.intro1_00002, R.drawable.intro1_00003, R.drawable.intro1_00004,
            R.drawable.intro1_00005, R.drawable.intro1_00006, R.drawable.intro1_00007, R.drawable.intro1_00008, R.drawable.intro1_00009,
            R.drawable.intro1_00010, R.drawable.intro1_00011, R.drawable.intro1_00012, R.drawable.intro1_00013, R.drawable.intro1_00014,
            R.drawable.intro1_00015, R.drawable.intro1_00016, R.drawable.intro1_00017, R.drawable.intro1_00018, R.drawable.intro1_00019,
            R.drawable.intro1_00020, R.drawable.intro1_00021, R.drawable.intro1_00022, R.drawable.intro1_00023, R.drawable.intro1_00024,
            R.drawable.intro1_00025, R.drawable.intro1_00026, R.drawable.intro1_00027, R.drawable.intro1_00028, R.drawable.intro1_00029, R.drawable.intro1_00030 };

    private Timer animationTimer = new Timer();
    private ImageView animeImageView;
    private Button btnClose;
    private int imageIndex = 0;

    public static IntroPageButtonListener buttonListener;

    public Intro1Fragment() {
        // Required empty public constructor
    }

    public static Intro1Fragment newInstance(IntroPageButtonListener callBack) {
        Intro1Fragment fragment = new Intro1Fragment();
        buttonListener = callBack;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_intro1, container, false);
        animeImageView = (ImageView) view.findViewById(R.id.intro_1_imageview);
        btnClose = (Button) view.findViewById(R.id.btn_intro_1_close);

        btnClose.setOnClickListener(this);
        onPageSelected();
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
