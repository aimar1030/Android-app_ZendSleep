package com.peter.zensleepfree.Fragments.Intros;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.peter.zensleepfree.Interfaces.IntroPageButtonListener;
import com.peter.zensleepfree.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Intro5Fragment extends Fragment implements View.OnClickListener {

    private Button btnClose, btnStart;
    public static IntroPageButtonListener buttonListener;

    public Intro5Fragment() {
        // Required empty public constructor
    }

    public static Intro5Fragment newInstance(IntroPageButtonListener callBack) {
        Intro5Fragment fragment = new Intro5Fragment();
        buttonListener = callBack;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_intro5, container, false);
        btnClose = (Button) view.findViewById(R.id.btn_intro_5_close);
        btnStart = (Button) view.findViewById(R.id.btn_start_now);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "Gotham-Light.ttf");
        btnStart.setTypeface(typeface);
        btnClose.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        buttonListener.onCloseClicked();
    }
}
