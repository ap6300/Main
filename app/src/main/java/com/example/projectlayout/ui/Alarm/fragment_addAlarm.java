package com.example.projectlayout.ui.Alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projectlayout.R;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static com.example.projectlayout.ui.Alarm.AlarmBroadcastReceiver.DESCRIPTION;
import static com.example.projectlayout.ui.Alarm.AlarmBroadcastReceiver.FRIDAY;
import static com.example.projectlayout.ui.Alarm.AlarmBroadcastReceiver.IMAGE;
import static com.example.projectlayout.ui.Alarm.AlarmBroadcastReceiver.MONDAY;
import static com.example.projectlayout.ui.Alarm.AlarmBroadcastReceiver.RECURRING;
import static com.example.projectlayout.ui.Alarm.AlarmBroadcastReceiver.SATURDAY;
import static com.example.projectlayout.ui.Alarm.AlarmBroadcastReceiver.SUNDAY;
import static com.example.projectlayout.ui.Alarm.AlarmBroadcastReceiver.THURSDAY;
import static com.example.projectlayout.ui.Alarm.AlarmBroadcastReceiver.TUESDAY;
import static com.example.projectlayout.ui.Alarm.AlarmBroadcastReceiver.WEDNESDAY;


public class fragment_addAlarm extends Fragment {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private ImageView mImageView;
    private boolean recInserted =false;
    private AlarmDatabase db;

    public fragment_addAlarm() {
        // Required empty public constructor
    }
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_alarm, container, false);

        final Calendar calendar = Calendar.getInstance();

        final TimePicker picker=(TimePicker)root.findViewById(R.id.timePicker1);

        final EditText description = (EditText) root.findViewById(R.id.description_add_alarm);
        final CheckBox repeat = (CheckBox) root.findViewById(R.id.repeat);

        final CheckBox mon = (CheckBox) root.findViewById(R.id.Mon);
        final CheckBox tue = (CheckBox) root.findViewById(R.id.Tue);
        final CheckBox wed = (CheckBox) root.findViewById(R.id.Wed);
        final CheckBox thur = (CheckBox) root.findViewById(R.id.Thur);
        final CheckBox fri = (CheckBox) root.findViewById(R.id.Fri);
        final CheckBox sat = (CheckBox) root.findViewById(R.id.Sat);
        final CheckBox sun = (CheckBox) root.findViewById(R.id.Sun);

        calendar.setTimeInMillis(System.currentTimeMillis());

        Button add = (Button) root.findViewById(R.id.add_button);


        final LinearLayout dayCheckBox = (LinearLayout) root.findViewById(R.id.repeat_day_checkBox);

        final View dayLine = (View) root.findViewById(R.id.day_line);

        db = new AlarmDatabase(getActivity());
        db.openReadable();


        repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dayCheckBox.setVisibility(View.VISIBLE);
                    dayLine.setVisibility(View.VISIBLE);
                } else {
                    dayCheckBox.setVisibility(View.GONE);
                    dayLine.setVisibility(View.GONE);
                }
            }
        });

        mImageView = root.findViewById(R.id.imageView_add_alarm);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {


                int alarmOn = 1;
                int hour = picker.getHour();
                int min = picker.getMinute();
                int recurring = 0;
                if(repeat.isChecked()){
                    recurring= 1;
                }

                int monday = 0;
                if(mon.isChecked()){
                    monday = 1;
                }

                int tuesday = 0;
                if(tue.isChecked()){
                    tuesday = 1;
                }

                int wednesday = 0;
                if(wed.isChecked()){
                    wednesday = 1;
                }

                int thursday = 0;
                if(thur.isChecked()){
                    thursday = 1;
                }

                int friday = 0;
                if(fri.isChecked()){
                    friday = 1;
                }

                int saturday = 0;
                if(sat.isChecked()){
                    saturday = 1;
                }

                int sunday = 0;
                if(sun.isChecked()){
                    sunday = 1;
                }


                if (description.getText() != null && (int) mImageView.getTag() == 1) {
                    recInserted=db.addRow(hour,min,description.getText().toString(),alarmOn,recurring,monday,tuesday,wednesday,thursday,friday,saturday,sunday,imageViewToByte(mImageView));

                    AlarmManager am;
                    am = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
                    Intent intent = new Intent(getContext(), AlarmBroadcastReceiver.class);
                    intent.putExtra(RECURRING, recurring);
                    intent.putExtra(MONDAY, mon.isChecked());
                    intent.putExtra(TUESDAY, tue.isChecked());
                    intent.putExtra(WEDNESDAY, wed.isChecked());
                    intent.putExtra(THURSDAY, thur.isChecked());
                    intent.putExtra(FRIDAY, fri.isChecked());
                    intent.putExtra(SATURDAY, sat.isChecked());
                    intent.putExtra(SUNDAY, sun.isChecked());
                    intent.putExtra(DESCRIPTION, description.getText().toString());
                    intent.putExtra(IMAGE,imageViewToByte(mImageView));

                    PendingIntent sender = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, min);

                    // if alarm time has already passed, increment day by 1
                    if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                    }

                    am.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),sender);


                }
                if (recInserted) {
                    Toast.makeText(getActivity(), "Success add row", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getActivity(), "Not Success add row", Toast.LENGTH_SHORT).show();
                }

                db.close();
            }
        });
        return root;
    }



    private static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    //Pick an image from the gallery
    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    //handle result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED) {
                //permission was granted
                pickImageFromGallery();
            } else {
                //permission was denied
                Toast.makeText(getActivity(), "Permission denied...!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //handle result of picked image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            //set image to image view
            mImageView.setImageURI(data.getData());
            mImageView.setTag(1);

        }
    }





}
