package com.example.projectlayout.ui.Alarm;

import android.annotation.SuppressLint;
import android.database.Cursor;
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

        GridView recyclerView = (GridView) root.findViewById(R.id.alarm_list);

        ArrayList<Alarm> list = new ArrayList<>();
        list.clear();


        AlarmDatabase db = new AlarmDatabase(getContext());
        db.openReadable();

        byte[] image = null;

        Cursor cursor = db.getData1("SELECT * FROM Alarm");

        while (cursor.moveToNext()) {

            int id = cursor.getInt(0);
            int hour= cursor.getInt(1);
            int min = cursor.getInt(2);;
            String description = cursor.getString(3);;

            boolean alarmOn = false;
            if(cursor.getInt(4) == 1){ alarmOn = true; }

            boolean recurring = false;
            if(cursor.getInt(5) == 1){ recurring = true; }

            boolean mon = false;
            if(cursor.getInt(6) == 1){ mon = true; }
            boolean tue = false;
            if(cursor.getInt(7) == 1){ tue = true; }
            boolean wed = false;
            if(cursor.getInt(8) == 1){ wed = true; }
            boolean thur = false;
            if(cursor.getInt(9) == 1){ thur = true;}
            boolean fri = false;
            if(cursor.getInt(10) == 1){ fri = true; }
            boolean sat = false;
            if(cursor.getInt(11) == 1){ sat = true; }
            boolean sun = false;
            if(cursor.getInt(12) == 1){ sun = true; }

            image = cursor.getBlob(13);

            list.add(new Alarm(id,hour,min,description,alarmOn,recurring,mon,tue,wed,thur,fri,sat,sun,image));


        }

        AlarmAdapter alarmAdapter = new AlarmAdapter(getContext(),list);
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



    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_alarm, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_alarm) {
            NavHostFragment.findNavController(AlarmFragment.this)
                    .navigate(R.id.action_nav_alarm_to_fragment_addAlarm);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
