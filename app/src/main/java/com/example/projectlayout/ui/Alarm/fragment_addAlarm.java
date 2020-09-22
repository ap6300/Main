package com.example.projectlayout.ui.Alarm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.projectlayout.R;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;


public class fragment_addAlarm extends Fragment {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private ImageView mImageView;
    private boolean recInserted =false;
    private AlarmDatabase db;
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_add_alarm, container, false);



        final TimePicker picker= root.findViewById(R.id.timePicker1);

        final EditText description = root.findViewById(R.id.description_add_alarm);
        final CheckBox repeat = root.findViewById(R.id.repeat);

        final CheckBox mon = root.findViewById(R.id.Mon);
        final CheckBox tue = root.findViewById(R.id.Tue);
        final CheckBox wed = root.findViewById(R.id.Wed);
        final CheckBox thur = root.findViewById(R.id.Thur);
        final CheckBox fri = root.findViewById(R.id.Fri);
        final CheckBox sat = root.findViewById(R.id.Sat);
        final CheckBox sun = root.findViewById(R.id.Sun);

        Button add = root.findViewById(R.id.add_button);
        final LinearLayout dayCheckBox = root.findViewById(R.id.repeat_day_checkBox);
        final View dayLine = root.findViewById(R.id.day_line);

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

        repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Repeat");
                    String[] week = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                    builder.setMultiChoiceItems(week, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            repeat.setChecked(false);
                         }
                    });
                     builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {

                         }
                     });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }


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
                }






                int alarmid = 0;
                alarmid=db.getOneAlarm(description.getText().toString());

                Alarm alarm = new Alarm(
                        alarmid,
                        hour,
                        min,
                        description.getText().toString(),
                        true,
                        repeat.isChecked(),
                        mon.isChecked(),
                        tue.isChecked(),
                        wed.isChecked(),
                        thur.isChecked(),
                        fri.isChecked(),
                        sat.isChecked(),
                        sun.isChecked(),
                        imageViewToByte(mImageView)
                );

                TextView test = root.findViewById(R.id.test);
                if(alarmid != 0){
                    test.setText(Integer.toString(alarmid));
                }
                alarm.schedule(getContext());

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
