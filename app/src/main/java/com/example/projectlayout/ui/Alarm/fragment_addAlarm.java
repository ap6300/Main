package com.example.projectlayout.ui.Alarm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectlayout.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class fragment_addAlarm extends Fragment {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private ImageView mImageView;
    private boolean recInserted =false;
    private AlarmDatabase db;
    private TimePicker picker;
    private EditText description;
    private CheckBox repeat;
    private int[] weekday = new int[7];
     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        final View root = inflater.inflate(R.layout.fragment_add_alarm, container, false);



        picker= root.findViewById(R.id.timePicker1);

        description = root.findViewById(R.id.description_add_alarm);
        repeat = root.findViewById(R.id.repeat);

        db = new AlarmDatabase(getActivity());
        db.openReadable();

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
                final TextView show_recurring = root.findViewById(R.id.show_recurring);
                if(isChecked) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Repeat");
                    final String[] week = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                    final String[] shortWeek = {"Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"};
                    final ArrayList<String> list = new ArrayList<>();
                    builder.setMultiChoiceItems(week, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if(isChecked){
                                weekday[which] = 1;
                            }else{
                                weekday[which] = 0;
                            }
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
                             list.clear();
                             for(int i = 0 ; i < week.length; i ++){
                                 if(weekday[i]==1){
                                     list.add(shortWeek[i]);
                                 }
                             }

                             StringBuilder out = new StringBuilder();
                             for(int i = 0; i < list.size(); i++)
                             {
                                 if(i!=list.size()-1) {
                                     out.append(list.get(i)).append(",");
                                 }else{
                                     out.append(list.get(i));
                                 }
                             }
                             show_recurring.setText(out);
                         }
                     });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{

                    show_recurring.setText("");
                    for(int j =0; j < weekday.length ;j++) {
                        weekday[j] = 0;
                    }

                }

            }
        });

        return root;
    }

    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_tick, menu);
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
            add();
            NavHostFragment.findNavController(fragment_addAlarm.this)
                    .navigate(R.id.action_fragment_addAlarm_to_nav_alarm);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void add(){
        int alarmOn = 1;
        int hour = picker.getHour();
        int min = picker.getMinute();
        int recurring = 0;
        if(repeat.isChecked()){
            recurring= 1;
        }

        if (description.getText() != null && (int) mImageView.getTag() == 1) {
            recInserted=db.addRow(hour,min,description.getText().toString(),alarmOn,recurring, weekday[0],weekday[1],weekday[2],weekday[3],weekday[4],weekday[5],weekday[6],Base64.encodeToString(imageViewToByte(mImageView),Base64.DEFAULT));
        }

        int alarmid = 0;
        alarmid=db.getOneAlarm(description.getText().toString());

        boolean mon = false;
        if(weekday[0]==1){mon=true;}
        boolean tue = false;
        if(weekday[1]==1){tue=true;}
        boolean wed = false;
        if(weekday[2]==1){wed=true;}
        boolean thur = false;
        if(weekday[3]==1){thur=true;}
        boolean fri = false;
        if(weekday[4]==1){fri=true;}
        boolean sat = false;
        if(weekday[5]==1){sat=true;}
        boolean sun = false;
        if(weekday[6]==1){sat=true;}

        Alarm alarm = new Alarm(
                alarmid,
                hour,
                min,
                description.getText().toString(),
                true,
                repeat.isChecked(),
                mon,
                tue,
                wed,
                thur,
                fri,
                sat,
                sun,
                Base64.encodeToString(imageViewToByte(mImageView),Base64.DEFAULT)
        );


        alarm.schedule(getContext());

        db.close();

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
