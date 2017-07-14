package com.peter.zensleepfree.CustomAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.peter.zensleepfree.CustomView.FontTextView;
import com.peter.zensleepfree.Model.SleepDBModel;
import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.Utils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by peter on 6/27/16.
 */
public class SleepDataListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SleepDBModel> sleepData;
    private int currentMonth = -1;

    public SleepDataListAdapter(Context context, ArrayList<SleepDBModel> sleepDBModels) {
        this.context = context;
        sleepData = sleepDBModels;
    }

    @Override
    public int getCount() {
        return sleepData.size();
    }

    @Override
    public SleepDBModel getItem(int position) {
        return sleepData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SleepDBModel sleepDBModel = sleepData.get(position);
        Calendar calendar = Calendar.getInstance();
        long time = (long)sleepDBModel.getStartTimeSec() * 1000;
        calendar.setTimeInMillis(time);
        int month = calendar.get(Calendar.MONTH);
//        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (currentMonth != month) {
                int nights = 0;
                float totalEffi = 0;
                for (int i = 0; i < sleepData.size(); i++) {
                    SleepDBModel data = sleepData.get(i);
                    long tTime = (long)data.getStartTimeSec() * 1000;
                    Calendar tCal = Calendar.getInstance();
                    tCal.setTimeInMillis(tTime);
                    if (currentMonth == -1) {
                        currentMonth = month;
                        nights++;
                        totalEffi += data.getEfficiency();
                    } else if (month == tCal.get(Calendar.MONTH)){
                        nights++;
                        totalEffi += data.getEfficiency();
                    }
                }
                currentMonth = month;
                convertView = vi.inflate(R.layout.notes_row_header, null);
                FontTextView txtMonth = (FontTextView) convertView.findViewById(R.id.text_result_row_header_month);
                FontTextView txtNights = (FontTextView) convertView.findViewById(R.id.text_result_row_header_nights);
                FontTextView txtTotalEffi = (FontTextView) convertView.findViewById(R.id.text_result_row_header_efficienty);

                txtMonth.setText(Utils.getDateFromSec(sleepDBModel.getStartTimeSec(), "MMM yyyy"));
                txtNights.setText(String.valueOf(nights) + " nights");
                txtTotalEffi.setText(String.valueOf((int)(totalEffi/nights * 10000)/100) + "%");

                FontTextView txtDate = (FontTextView)convertView.findViewById(R.id.note_row_header_date);
                FontTextView txtStartDuration = (FontTextView)convertView.findViewById(R.id.note_row_header_start_duration);
                FontTextView txtEfficient = (FontTextView) convertView.findViewById(R.id.note_row_header_efficienty);
                RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.note_header_container);

                Calendar cal = Calendar.getInstance();
                int cMon = cal.get(Calendar.MONTH);
                if (cMon == month) {
                    container.setBackgroundColor(Color.GRAY);
                } else {
                    container.setBackgroundColor(Color.parseColor("#dddddd"));
                }

                ImageView imgStatus1 = (ImageView)convertView.findViewById(R.id.note_row_header_status_img1);
                ImageView imgStatus2 = (ImageView)convertView.findViewById(R.id.note_row_header_status_img2);
                ImageView imgStatus3 = (ImageView)convertView.findViewById(R.id.note_row_header_status_img3);
                ImageView imgStatus4 = (ImageView)convertView.findViewById(R.id.note_row_header_status_img4);
                ImageView imgStatus5 = (ImageView)convertView.findViewById(R.id.note_row_header_status_img5);
                ImageView imgStatus6 = (ImageView)convertView.findViewById(R.id.note_row_header_status_img6);

                txtDate.setText(Utils.getDateFromSec(sleepDBModel.getStartTimeSec(), "EEE, MMM d"));
                txtStartDuration.setText(Utils.getDateFromSec(sleepDBModel.getStartTimeSec(), "h:mm a") + " | " + Utils.getHoursMinutes(sleepDBModel.getElapsedSec()));

                txtEfficient.setText(String.valueOf((int)(sleepDBModel.getEfficiency() * 10000)/100) + "%");
                txtEfficient.setBackgroundResource(Utils.getColorForEfficienty((int) (sleepDBModel.getEfficiency() * 10000)/100));

                ImageView[] vs = { imgStatus1, imgStatus2, imgStatus3, imgStatus4, imgStatus5, imgStatus6 };
                int idx = 0;
                int bitAnd = sleepDBModel.getStatus() & 0x0001;
                if (bitAnd != 0) {
                    vs[idx++].setImageResource(R.drawable.status_1_on);
                }
                bitAnd = sleepDBModel.getStatus() & 0x0002;
                if (bitAnd != 0) {
                    vs[idx++].setImageResource(R.drawable.status_2_on);
                }

                bitAnd = sleepDBModel.getStatus() & 0x0004;
                if (bitAnd != 0) {
                    vs[idx++].setImageResource(R.drawable.status_3_on);
                }
                bitAnd = sleepDBModel.getStatus() & 0x0008;
                if (bitAnd != 0) {
                    vs[idx++].setImageResource(R.drawable.status_4_on);
                }
                bitAnd = sleepDBModel.getStatus() & 0x0010;
                if (bitAnd != 0) {
                    vs[idx++].setImageResource(R.drawable.status_5_on);
                }
                bitAnd = sleepDBModel.getStatus() & 0x0020;
                if (bitAnd != 0) {
                    vs[idx++].setImageResource(R.drawable.status_6_on);
                }
            } else {
                convertView = vi.inflate(R.layout.notes_row, null);
                FontTextView txtDate = (FontTextView)convertView.findViewById(R.id.note_row_date);
                FontTextView txtStartDuration = (FontTextView)convertView.findViewById(R.id.note_row_start_duration);
                FontTextView txtEfficient = (FontTextView) convertView.findViewById(R.id.note_row_efficienty);
                RelativeLayout container = (RelativeLayout) convertView.findViewById(R.id.note_cell_back);

                ImageView imgStatus1 = (ImageView)convertView.findViewById(R.id.note_row_status_img1);
                ImageView imgStatus2 = (ImageView)convertView.findViewById(R.id.note_row_status_img2);
                ImageView imgStatus3 = (ImageView)convertView.findViewById(R.id.note_row_status_img3);
                ImageView imgStatus4 = (ImageView)convertView.findViewById(R.id.note_row_status_img4);
                ImageView imgStatus5 = (ImageView)convertView.findViewById(R.id.note_row_status_img5);
                ImageView imgStatus6 = (ImageView)convertView.findViewById(R.id.note_row_status_img6);

                Calendar cal = Calendar.getInstance();
                int cMon = cal.get(Calendar.MONTH);
                if (cMon == month) {
                    container.setBackgroundColor(Color.GRAY);
                } else {
                    container.setBackgroundColor(Color.parseColor("#dddddd"));
                }
                txtDate.setText(Utils.getDateFromSec(sleepDBModel.getStartTimeSec(), "EEE, MMM d"));
                txtStartDuration.setText(Utils.getDateFromSec(sleepDBModel.getStartTimeSec(), "h:mm a") + " | " + Utils.getHoursMinutes(sleepDBModel.getElapsedSec()));

                txtEfficient.setText(String.valueOf((int)(sleepDBModel.getEfficiency() * 10000)/100) + "%");
                txtEfficient.setBackgroundResource(Utils.getColorForEfficienty((int) (sleepDBModel.getEfficiency() * 10000)/100));

                ImageView[] vs = { imgStatus1, imgStatus2, imgStatus3, imgStatus4, imgStatus5, imgStatus6 };
                int idx = 0;
                int bitAnd = sleepDBModel.getStatus() & 0x0001;
                if (bitAnd != 0) {
                    vs[idx++].setImageResource(R.drawable.status_1_on);
                }
                bitAnd = sleepDBModel.getStatus() & 0x0002;
                if (bitAnd != 0) {
                    vs[idx++].setImageResource(R.drawable.status_2_on);
                }

                bitAnd = sleepDBModel.getStatus() & 0x0004;
                if (bitAnd != 0) {
                    vs[idx++].setImageResource(R.drawable.status_3_on);
                }
                bitAnd = sleepDBModel.getStatus() & 0x0008;
                if (bitAnd != 0) {
                    vs[idx++].setImageResource(R.drawable.status_4_on);
                }
                bitAnd = sleepDBModel.getStatus() & 0x0010;
                if (bitAnd != 0) {
                    vs[idx++].setImageResource(R.drawable.status_5_on);
                }
                bitAnd = sleepDBModel.getStatus() & 0x0020;
                if (bitAnd != 0) {
                    vs[idx++].setImageResource(R.drawable.status_6_on);
                }
            }
//        }
        return convertView;
    }

    public class ViewHolder {

    }
}
