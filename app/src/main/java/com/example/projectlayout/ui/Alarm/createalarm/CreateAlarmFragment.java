package com.example.projectlayout.ui.Alarm.createalarm;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;


import com.example.projectlayout.R;
import com.example.projectlayout.ui.Alarm.data.Alarm;

import java.io.File;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAlarmFragment extends Fragment {
    public static final int IMAGE_GALLERY_REQUEST = 20;
    @BindView(R.id.fragment_createalarm_timePicker) TimePicker timePicker;
    @BindView(R.id.fragment_createalarm_title) EditText title;
    @BindView(R.id.fragment_createalarm_scheduleAlarm) Button scheduleAlarm;
    @BindView(R.id.fragment_createalarm_recurring) CheckBox recurring;
    @BindView(R.id.fragment_createalarm_checkMon) CheckBox mon;
    @BindView(R.id.fragment_createalarm_checkTue) CheckBox tue;
    @BindView(R.id.fragment_createalarm_checkWed) CheckBox wed;
    @BindView(R.id.fragment_createalarm_checkThu) CheckBox thu;
    @BindView(R.id.fragment_createalarm_checkFri) CheckBox fri;
    @BindView(R.id.fragment_createalarm_checkSat) CheckBox sat;
    @BindView(R.id.fragment_createalarm_checkSun) CheckBox sun;
    //@BindView(R.id.fragment_createalarm_selectphoto) Button selectphoto;
    @BindView(R.id.fragment_createalarm_recurring_options) LinearLayout recurringOptions;

    private CreateAlarmViewModel createAlarmViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createAlarmViewModel = ViewModelProviders.of(this).get(CreateAlarmViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_createalarm, container, false);

        ButterKnife.bind(this, view);

        recurring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recurringOptions.setVisibility(View.VISIBLE);
                } else {
                    recurringOptions.setVisibility(View.GONE);
                }
            }
        });

        scheduleAlarm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                scheduleAlarm();
                Navigation.findNavController(v).navigate(R.id.action_createAlarmFragment_to_alarmsListFragment);
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void scheduleAlarm() {
        int alarmId = new Random().nextInt(Integer.MAX_VALUE);

        Alarm alarm = new Alarm(
                alarmId,
                TimePickerUtil.getTimePickerHour(timePicker),
                TimePickerUtil.getTimePickerMinute(timePicker),
                title.getText().toString(),
                System.currentTimeMillis(),
                true,
                recurring.isChecked(),
                mon.isChecked(),
                tue.isChecked(),
                wed.isChecked(),
                thu.isChecked(),
                fri.isChecked(),
                sat.isChecked(),
                sun.isChecked()
                //selectphoto.toString()
        );

        createAlarmViewModel.insert(alarm);

        alarm.schedule(getContext());
    }

    public void onImgGalleryClick (View v) {
        //invoke img gallery using implicit intent.
        Intent pickphotointent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getParent();
        //get URI representation
        Uri data = Uri.parse(pictureDirectoryPath);

        //set data and type
        pickphotointent.setDataAndType(data,"image/*");

        startActivityForResult(pickphotointent, IMAGE_GALLERY_REQUEST);


    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {

        }
    } */
}
