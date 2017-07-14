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
 * Created by peter on 6/20/16.
 */
public class AlarmListAdapter extends BaseAdapter {

    private Context context;
    private int selectedPosition = 0;

    public AlarmListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Constant.ALARM_SOUNDS.length;
    }

    @Override
    public String getItem(int position) {
        return Constant.ALARM_SOUNDS[position];
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
            v = vi.inflate(R.layout.row_alarm_sound, null);
        }
        FontTextView tv = (FontTextView)v.findViewById(R.id.row_text_alarm_name);
        tv.setText(getItem(position));
        RadioButton r = (RadioButton)v.findViewById(R.id.row_radio_sound);
        r.setChecked(position == selectedPosition);
        r.setTag(position);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = (Integer)view.getTag();
                notifyDataSetChanged();
            }
        });
        return v;
    }

    public int getSelectedSong() {
        return selectedPosition;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }
}
