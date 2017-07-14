package com.peter.zensleepfree.Fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.peter.zensleepfree.CustomView.FontTextView;
import com.peter.zensleepfree.CustomView.StatsGraphView;
import com.peter.zensleepfree.Model.SleepDBModel;
import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.SleepDBHelper;
import com.peter.zensleepfree.UtilsClass.Utils;
import com.transitionseverywhere.TransitionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import at.grabner.circleprogress.CircleProgressView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment implements View.OnClickListener {

    private Button btnWeek, btnMonth, btnYear, btnOverall, btnRight, btnLeft;
    private FontTextView txtDuration, txtTrackedNightValue, txtAvgDurationValue, txtLongestNight, txtLongestDate, txtShortestNight, txtShortestDate,
            txtAvgEfficiencyValue, txtTimeSleepingValue, txtDate_1, txtTime_1, txtDate_2, txtTime_2, txtDate_3,
            txtTime_3, txtDate_4, txtTime_4, txtDate_5, txtTime_5, txtDate_6, txtTime_6;

    private TextView txtUnderline;
    private RelativeLayout durationContainer;
    private CircleProgressView avgEfficiency, sleepingEfficiency;
    private StatsGraphView statsGraph;

    private static int OVERALL = 0;
    private static int YEARLY = 1;
    private static int MONTHLY = 2;
    private static int WEEKLY = 3;

    private int currentDateTerms = 0;
    private int currentYear, currentMonth, currentWeek;
    private long firstDayOfCurrentWeek;

    private ViewGroup transitionsContainer;

    private SleepDBHelper sleepDBHelper;
    private ArrayList<SleepDBModel> sleepDBModels = new ArrayList<>();
    private ArrayList<SleepDBModel> curSleeps = new ArrayList<>();

    public StatsFragment() {
        // Required empty public constructor
    }

    public static StatsFragment newInstance() {

        StatsFragment fragment = new StatsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        btnWeek = (Button) view.findViewById(R.id.btn_state_week);
        btnMonth = (Button) view.findViewById(R.id.btn_state_month);
        btnYear = (Button) view.findViewById(R.id.btn_state_year);
        btnOverall = (Button) view.findViewById(R.id.btn_state_overall);
        btnRight = (Button) view.findViewById(R.id.btn_state_newer);
        btnLeft = (Button) view.findViewById(R.id.btn_state_older);

        txtDuration = (FontTextView) view.findViewById(R.id.text_state_current);
        txtUnderline = (TextView) view.findViewById(R.id.text_state_underline);
        txtTrackedNightValue = (FontTextView) view.findViewById(R.id.text_state_tracked_night_value);
        txtAvgDurationValue = (FontTextView) view.findViewById(R.id.text_state_avg_duration_value);
        txtLongestNight = (FontTextView) view.findViewById(R.id.text_state_longest);
        txtLongestDate = (FontTextView) view.findViewById(R.id.text_state_longest_value);
        txtShortestNight = (FontTextView) view.findViewById(R.id.text_state_shortest);
        txtShortestDate = (FontTextView) view.findViewById(R.id.text_state_shortest_value);
        txtAvgEfficiencyValue = (FontTextView) view.findViewById(R.id.text_state_avg_efficiency_value);
        txtTimeSleepingValue = (FontTextView) view.findViewById(R.id.text_sleeping_time_value);
        txtDate_1 = (FontTextView) view.findViewById(R.id.text_state_1_date);
        txtTime_1 = (FontTextView) view.findViewById(R.id.text_state_1_time);
        txtDate_2 = (FontTextView) view.findViewById(R.id.text_state_2_date);
        txtTime_2 = (FontTextView) view.findViewById(R.id.text_state_2_time);
        txtDate_3 = (FontTextView) view.findViewById(R.id.text_state_3_date);
        txtTime_3 = (FontTextView) view.findViewById(R.id.text_state_3_time);
        txtDate_4 = (FontTextView) view.findViewById(R.id.text_state_4_date);
        txtTime_4 = (FontTextView) view.findViewById(R.id.text_state_4_time);
        txtDate_5 = (FontTextView) view.findViewById(R.id.text_state_5_date);
        txtTime_5 = (FontTextView) view.findViewById(R.id.text_state_5_time);
        txtDate_6 = (FontTextView) view.findViewById(R.id.text_state_6_date);
        txtTime_6 = (FontTextView) view.findViewById(R.id.text_state_6_time);
        durationContainer = (RelativeLayout) view.findViewById(R.id.duration_container);
        avgEfficiency = (CircleProgressView) view.findViewById(R.id.state_avg_efficienty);
        sleepingEfficiency = (CircleProgressView) view.findViewById(R.id.state_time_sleeping);
        statsGraph = (StatsGraphView) view.findViewById(R.id.stats_graph_view);

        transitionsContainer = (ViewGroup) view.findViewById(R.id.header);

        initUI();

        return view;
    }

    private void initUI() {
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "Gotham-Light.ttf");
        btnWeek.setTypeface(typeface);
        btnMonth.setTypeface(typeface);
        btnYear.setTypeface(typeface);
        btnOverall.setTypeface(typeface);
        btnRight.setTypeface(typeface);
        btnLeft.setTypeface(typeface);

        btnLeft.setOnClickListener(this);
        btnWeek.setOnClickListener(this);
        btnMonth.setOnClickListener(this);
        btnYear.setOnClickListener(this);
        btnOverall.setOnClickListener(this);
        btnRight.setOnClickListener(this);

        durationContainer.setVisibility(View.GONE);

        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        firstDayOfCurrentWeek = System.currentTimeMillis() - 1000 * 3600 * 24 * (dayOfWeek - 1);

        sleepDBHelper = new SleepDBHelper(getActivity());
        sleepDBModels = sleepDBHelper.getAllData();
        updateDateTerms();
        updateStatsData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_state_week:
                animateUnderline(WEEKLY);
                break;
            case R.id.btn_state_month:
                Calendar calendar = Calendar.getInstance();
                currentYear = calendar.get(Calendar.YEAR);
                currentMonth = calendar.get(Calendar.MONTH);
                animateUnderline(MONTHLY);
                break;
            case R.id.btn_state_year:
                animateUnderline(YEARLY);
                break;
            case R.id.btn_state_overall:
                animateUnderline(OVERALL);
                break;
            case R.id.btn_state_newer:
                if (currentDateTerms == YEARLY) {
                    currentYear++;
                } else if (currentDateTerms == MONTHLY) {
                    currentMonth++;
                    if (currentMonth > 12) {
                        currentMonth = 1;
                        currentYear++;
                    }
                } else if (currentDateTerms == WEEKLY) {
                    firstDayOfCurrentWeek = firstDayOfCurrentWeek + 3600 * 24 * 7 * 1000;
                }
                break;
            case R.id.btn_state_older:
                if (currentDateTerms == YEARLY) {
                    currentYear--;
                } else if (currentDateTerms == MONTHLY) {
                    currentMonth--;
                    if (currentMonth < 1) {
                        currentMonth = 12;
                        currentYear--;
                    }
                } else if (currentDateTerms == WEEKLY) {
                    firstDayOfCurrentWeek = firstDayOfCurrentWeek - 3600 * 24 * 7 * 1000;
                }
                break;
        }
        updateDateTerms();
        updateStatsData();
    }

    private void animateUnderline(int action) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) txtUnderline.getLayoutParams();
        params.width = btnWeek.getWidth();
        TransitionManager.beginDelayedTransition(transitionsContainer);
        switch (action) {
            case 0:
                durationContainer.setVisibility(View.GONE);
                params.setMargins(btnWeek.getWidth() * 3, 0, 0, 0);
                txtUnderline.setLayoutParams(params);
                break;
            case 1:
                durationContainer.setVisibility(View.VISIBLE);
                params.setMargins(btnWeek.getWidth() * 2, 0, btnWeek.getWidth(), 0);
                txtUnderline.setLayoutParams(params);
                break;
            case 2:
                durationContainer.setVisibility(View.VISIBLE);
                params.setMargins(btnWeek.getWidth(), 0, btnWeek.getWidth() * 2, 0);
                txtUnderline.setLayoutParams(params);
                break;
            case 3:
                durationContainer.setVisibility(View.VISIBLE);
                params.setMargins(0, 0, btnWeek.getWidth() * 3, 0);
                txtUnderline.setLayoutParams(params);
                break;
        }
        currentDateTerms = action;
    }

    private void loadSleepData() {
        curSleeps.clear();
        if (currentDateTerms == OVERALL) {
            for (int i = 0; i < sleepDBModels.size(); i++) {
                curSleeps.add(sleepDBModels.get(i));
            }
        } else if (currentDateTerms == YEARLY) {
            Calendar cal = Calendar.getInstance();
            for (int i = 0; i < sleepDBModels.size(); i++) {
                long startSec = ((long)sleepDBModels.get(i).startTimeSec) * 1000;
                cal.setTimeInMillis(startSec);
                int year = cal.get(Calendar.YEAR);
                if (year == Integer.parseInt(txtDuration.getText().toString())) {
                    curSleeps.add(sleepDBModels.get(i));
                    Log.d("StatsFragment", "Add one data for this year");
                }
            }
        } else if (currentDateTerms == MONTHLY) {
            Calendar cal = Calendar.getInstance();
            Calendar cCal = Calendar.getInstance();
            Calendar cCal1 = Calendar.getInstance();
            for (int i = 0; i < sleepDBModels.size(); i++) {
                long startSec = ((long)sleepDBModels.get(i).startTimeSec) * 1000;
                cal.setTimeInMillis(startSec);
                cCal.set(currentYear, currentMonth + 1, 1);
                cCal1.set(currentYear, currentMonth, 1);
                if (startSec >= cCal1.getTimeInMillis() && startSec < cCal.getTimeInMillis()) {
                    curSleeps.add(sleepDBModels.get(i));
                    Log.d("StatsFragment", "Add one data for current month");
                }
            }
        } else if (currentDateTerms == WEEKLY) {
            Calendar cal = Calendar.getInstance();
            Calendar cCal = Calendar.getInstance();
            Calendar cCal1 = Calendar.getInstance();
            for (int i = 0; i < sleepDBModels.size(); i++) {
                long startSec = ((long)sleepDBModels.get(i).startTimeSec) * 1000;
                cal.setTimeInMillis(startSec);
                cCal.setTimeInMillis(firstDayOfCurrentWeek);
                cCal1.setTimeInMillis(firstDayOfCurrentWeek + 7 * 24 * 3600 * 1000);
                if (startSec >= cCal.getTimeInMillis() && startSec < cCal1.getTimeInMillis()) {
                    curSleeps.add(sleepDBModels.get(i));
                    Log.d("StatsFragment", "Add one data for current month");
                }
            }
        }
    }

    private void updateStatsData() {
        txtTrackedNightValue.setText(String.valueOf(curSleeps.size()));
        double[] sumEffDay = {0, 0, 0, 0, 0, 0, 0};
        double[] sumDurDay = {0, 0, 0, 0, 0, 0, 0};
        Calendar cal = Calendar.getInstance();
        if (curSleeps.size() > 0) {
            SleepDBModel longestNight = curSleeps.get(0);
            SleepDBModel shortestNight = curSleeps.get(0);

            double sumEffi = longestNight.getEfficiency();
            double sumDur = longestNight.elapsedSec;
            int[] numDay = {0, 0, 0, 0, 0, 0, 0};
            int w = 0;
            cal.setTimeInMillis(((long)longestNight.startTimeSec) * 1000);
            w = cal.get(Calendar.DAY_OF_WEEK);
            if (1 <= w && w <= 7) {
                sumEffDay[w-1] = longestNight.getEfficiency();
                sumDurDay[w-1] = longestNight.getEfficiency();
                numDay[w-1]++;
            }
            for (int i = 0; i < curSleeps.size(); i++) {
                SleepDBModel s = curSleeps.get(i);
                sumDur += s.elapsedSec;
                sumEffi += s.getEfficiency();

                cal.setTimeInMillis(((long)longestNight.startTimeSec) * 1000);
                w = cal.get(Calendar.DAY_OF_WEEK);
                if (1 <= w && w <= 7) {
                    sumEffDay[w-1] = s.getEfficiency();
                    sumDurDay[w-1] = s.getEfficiency();
                    numDay[w-1]++;
                }
                if (longestNight.elapsedSec < s.elapsedSec)
                    longestNight = s;
                if (shortestNight.elapsedSec > s.elapsedSec)
                    shortestNight = s;
            }
            double nNights = curSleeps.size();
            txtAvgDurationValue.setText(Utils.getHoursMinutes((int)(sumDur/nNights)));
            txtLongestDate.setText(Utils.getHoursMinutes(longestNight.elapsedSec));
            txtShortestDate.setText(Utils.getHoursMinutes(shortestNight.elapsedSec));
            avgEfficiency.setValue((float) (sumEffi/nNights * 100));
            txtAvgEfficiencyValue.setText(String.valueOf((int)(sumEffi/nNights * 10000)/100) + "%");
            sleepingEfficiency.setValue((float) (sumDur/nNights/(24.0 * 3600) * 100));
            txtTimeSleepingValue.setText(String.valueOf((int)(sumDur/nNights/(24 * 3600) * 10000)/100) + "%");
            txtLongestNight.setText("LONGEST NIGHT\n" + Utils.getDateFromSec(longestNight.startTimeSec, "dd/MM/yyyy"));
            txtShortestNight.setText("SHORTEST NIGHT\n" + Utils.getDateFromSec(shortestNight.startTimeSec, "dd/MM/yyyy"));

            //Calculate avg
            for (int i = 0; i < 7; i++) {
                if (numDay[i] > 0) {
                    sumEffDay[i] /= numDay[i];
                    sumDurDay[i] /= numDay[i];
                }
            }
            TextView[] txtBedTimes = {txtTime_6, txtTime_5, txtTime_4, txtTime_3, txtTime_2, txtTime_1};
            TextView[] txtBedDates = {txtDate_6, txtDate_5, txtDate_4, txtDate_3, txtDate_2, txtDate_1};
            int baseIndex = curSleeps.size() - 6;
            for (int i = 0; i < 6; i++) {
                int idx = baseIndex + i;
                if (idx >= 0 && idx < curSleeps.size()) {
                    txtBedDates[i].setText(Utils.getDateFromSec(curSleeps.get(idx).startTimeSec, "MMM d"));
                    txtBedTimes[i].setText(Utils.getDateFromSec(curSleeps.get(idx).startTimeSec, "HH:mm"));
                } else {
                    txtBedDates[i].setText("");
                    txtBedTimes[i].setText("");
                }
            }

            statsGraph.setValues(sumEffDay, sumDurDay);
        } else {
            txtAvgDurationValue.setText("0h 0min");
            txtLongestDate.setText("0h 0min");
            txtLongestNight.setText("LONGEST NIGHT");
            txtShortestDate.setText("0h 0min");
            txtShortestNight.setText("SHORTEST NIGHT");
            avgEfficiency.setValue(0);
            sleepingEfficiency.setValue(0);
            txtAvgEfficiencyValue.setText("0%");
            txtTimeSleepingValue.setText("0%");
            TextView[] txtBedTimes = {txtTime_6, txtTime_5, txtTime_4, txtTime_3, txtTime_2, txtTime_1};
            TextView[] txtBedDates = {txtDate_6, txtDate_5, txtDate_4, txtDate_3, txtDate_2, txtDate_1};
            for (int i = 0; i < 6; i++) {
                txtBedDates[i].setText("");
                txtBedTimes[i].setText("");
            }
        }
    }

    private void updateDateTerms() {
        Calendar calendar = Calendar.getInstance();
        if (currentDateTerms == OVERALL) {

        } else if (currentDateTerms == YEARLY) {
            txtDuration.setText(String.valueOf(currentYear));
            if (calendar.get(Calendar.YEAR) <= currentYear) {
                btnRight.setVisibility(View.INVISIBLE);
            } else {
                btnRight.setVisibility(View.VISIBLE);
            }
        } else if (currentDateTerms == MONTHLY) {

            Calendar tCal = Calendar.getInstance();
            tCal.set(currentYear, currentMonth, 1);
            SimpleDateFormat formatter = new SimpleDateFormat("MMM yyyy");
            txtDuration.setText(formatter.format(tCal.getTime()));

            int month = currentMonth + 1;
            Calendar nCal = Calendar.getInstance();
            nCal.set(currentYear, month, 1);

            Calendar cCal = Calendar.getInstance();
            if (cCal.compareTo(nCal) <= 0) {
                btnRight.setVisibility(View.INVISIBLE);
            } else {
                btnRight.setVisibility(View.VISIBLE);
            }
        } else {
            String dur = Utils.getDateFromSec((int) (firstDayOfCurrentWeek/1000), "MMM d, yyyy") + " - " + Utils.getDateFromSec((int) ((firstDayOfCurrentWeek + 6 * 24 * 3600 * 1000)/1000), "MMM d, yyyy");
            txtDuration.setText(dur);
            if (System.currentTimeMillis() - firstDayOfCurrentWeek >  7 * 24 * 3600 * 1000) {
                btnRight.setVisibility(View.VISIBLE);
            } else {
                btnRight.setVisibility(View.INVISIBLE);
            }
        }
        loadSleepData();
    }
}
