package com.peter.zensleepfree.Fragments.Intros;


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
public class Intro4Fragment extends Fragment implements View.OnClickListener {

    private Button btnClose;
    public static IntroPageButtonListener buttonListener;

    public Intro4Fragment() {
        // Required empty public constructor
    }

    public static Intro4Fragment newInstance(IntroPageButtonListener callBack) {
        Intro4Fragment fragment = new Intro4Fragment();
        buttonListener = callBack;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_intro4, container, false);
        btnClose = (Button) view.findViewById(R.id.btn_intro_4_close);
        btnClose.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        buttonListener.onCloseClicked();
    }
}
