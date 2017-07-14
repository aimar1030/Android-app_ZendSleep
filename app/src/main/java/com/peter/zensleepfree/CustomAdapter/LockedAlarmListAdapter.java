package com.peter.zensleepfree.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.peter.zensleepfree.CustomView.FontTextView;
import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.Constant;

/**
 * Created by peter on 8/18/16.
 */
public class LockedAlarmListAdapter extends BaseAdapter {

    private Context context;

    public LockedAlarmListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Constant.ALARM_PAID_SOUNDS.length;
    }

    @Override
    public String getItem(int position) {
        return Constant.ALARM_PAID_SOUNDS[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.locked_alarm_cell, null);
        }
        FontTextView tv = (FontTextView)v.findViewById(R.id.row_text_lock_alarm_name);
        tv.setText(getItem(position));
        return v;
    }
}
