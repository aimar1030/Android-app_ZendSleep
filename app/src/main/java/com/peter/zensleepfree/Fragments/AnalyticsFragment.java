package com.peter.zensleepfree.Fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.peter.zensleepfree.Activity.UpgradeActivity;
import com.peter.zensleepfree.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalyticsFragment extends Fragment {

    private TextView titleText, txtUnlock;
    private Button btnUpgrade;

    public AnalyticsFragment() {
        // Required empty public constructor
    }

    public static AnalyticsFragment newInstance() {
        AnalyticsFragment fragment = new AnalyticsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_analytics, container, false);

        titleText = (TextView) view.findViewById(R.id.analytics_title);
        txtUnlock = (TextView) view.findViewById(R.id.txt_unlock);
        btnUpgrade = (Button) view.findViewById(R.id.btn_upgrade);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "Gotham-Book.ttf");
        titleText.setTypeface(typeface);
        txtUnlock.setTypeface(typeface);
        btnUpgrade.setTypeface(typeface);

        btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), UpgradeActivity.class));
            }
        });

        return view;
    }

}
