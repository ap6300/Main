package com.example.projectlayout.ui.Alarm;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectlayout.R;

import java.util.ArrayList;

public class AlarmFragment extends Fragment {



    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_alarm, container, false);
        setHasOptionsMenu(true);

        GridView recyclerView = root.findViewById(R.id.alarm_list);

        ArrayList<Alarm> list = new ArrayList<>();
        list.clear();


        final AlarmDatabase db = new AlarmDatabase(getContext());
        db.openReadable();



       list = db.getAlarm();

        AlarmAdapter alarmAdapter = new AlarmAdapter(getContext(), list, new AlarmAdapter.alarmInterface() {
            @Override
            public void onToggle(Alarm alarm) {
                if(alarm.isAlarmOn()){
                    alarm.cancelAlarm(getContext());
                    alarm.setAlarmOn(false);
                    db.updateAlarm(0,alarm.getDescription());
                }else{
                    alarm.schedule(getContext());
                    alarm.setAlarmOn(true);
                    db.updateAlarm(1,alarm.getDescription());
                }

            }
        });
        recyclerView.setAdapter(alarmAdapter);

        alarmAdapter.notifyDataSetChanged();
        db.close();

        recyclerView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle result = new Bundle();
                TextView hi = view.findViewById(R.id.item_alarm_title);
                result.putString("key", (String) hi.getText());
                getParentFragmentManager().setFragmentResult("key", result);
                NavHostFragment.findNavController(AlarmFragment.this).navigate(R.id.action_nav_alarm_to_fragment_editAlarm);

            }
        });
        return root;
    }



    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_addicon, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_toolbar) {
            NavHostFragment.findNavController(AlarmFragment.this)
                    .navigate(R.id.action_nav_alarm_to_fragment_addAlarm);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
