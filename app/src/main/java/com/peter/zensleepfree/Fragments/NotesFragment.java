package com.peter.zensleepfree.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.peter.zensleepfree.CustomAdapter.SleepDataListAdapter;
import com.peter.zensleepfree.Interfaces.CustomButtonClickListener;
import com.peter.zensleepfree.Model.SleepDBModel;
import com.peter.zensleepfree.R;
import com.peter.zensleepfree.UtilsClass.SleepDBHelper;
import com.peter.zensleepfree.UtilsClass.Utils;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment implements AdapterView.OnItemClickListener {

//    private TextView titleText;
    private ListView sleepDataList;

    private SleepDBHelper sleepDBHelper;
    private ArrayList<SleepDBModel> sleepDBModels = new ArrayList<>();

    private static CustomButtonClickListener customButtonClickListener;

    public NotesFragment() {
        // Required empty public constructor
    }

    public static NotesFragment newInstance(CustomButtonClickListener callBack) {

        NotesFragment fragment = new NotesFragment();
        customButtonClickListener = callBack;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        sleepDataList = (ListView) view.findViewById(R.id.sleep_list_view);

        sleepDBHelper = new SleepDBHelper(getActivity());
        sleepDBModels = sleepDBHelper.getAllData();
        sleepDataList.setAdapter(new SleepDataListAdapter(getActivity(), sleepDBModels));

        sleepDataList.setOnItemClickListener(this);
//        if (Utils.getDriveSync(getActivity())) {
//            GoogleDriveClient.getDriveClient(getActivity()).connectDrive();
//        }
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        customButtonClickListener.onSleepDataClicked(sleepDBModels.get(position));
    }
}
