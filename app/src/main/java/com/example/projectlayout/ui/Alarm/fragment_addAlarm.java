package com.example.projectlayout.ui.Alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;

import com.example.projectlayout.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_addAlarm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_addAlarm extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_addAlarm() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_addAlarm.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_addAlarm newInstance(String param1, String param2) {
        fragment_addAlarm fragment = new fragment_addAlarm();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_alarm, container, false);

        final Calendar calendar = Calendar.getInstance();

        final TimePicker picker=(TimePicker)root.findViewById(R.id.timePicker1);

        final TextView time = (TextView) root.findViewById(R.id.time);
        final TextView hour = (TextView) root.findViewById(R.id.hour);

        calendar.setTimeInMillis(System.currentTimeMillis());

        Button add = (Button) root.findViewById(R.id.add_button);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.setText(Integer.toString(picker.getMinute()));
                hour.setText(Integer.toString(picker.getHour()));
            }
        });


        return root;
    }
}
