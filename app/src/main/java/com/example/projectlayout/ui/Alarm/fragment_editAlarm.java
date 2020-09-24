package com.example.projectlayout.ui.Alarm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectlayout.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_editAlarm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_editAlarm extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_editAlarm() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_editAlarm.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_editAlarm newInstance(String param1, String param2) {
        fragment_editAlarm fragment = new fragment_editAlarm();
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

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    private AlarmDatabase db;
    private ArrayList<Alarm> list;
    private TimePicker picker;
    private EditText des;
    private CheckBox repeat;
    private CheckBox monday;
    private CheckBox tuesday;
    private CheckBox wednesday;
    private CheckBox thursday;
    private CheckBox friday;
    private CheckBox saturday;
    private CheckBox sunday;
    private int[] id = {0};
    private boolean[] alarmOn = {false};
    private ImageView imageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View root = inflater.inflate(R.layout.fragment_edit_alarm, container, false);


        picker=(TimePicker)root.findViewById(R.id.timePicker1_edit);
        des = (EditText) root.findViewById(R.id.description_edit_alarm);
        repeat = (CheckBox) root.findViewById(R.id.repeat_edit);

        monday = (CheckBox) root.findViewById(R.id.Mon_edit);
        tuesday = (CheckBox) root.findViewById(R.id.Tue_edit);
        wednesday = (CheckBox) root.findViewById(R.id.Wed_edit);
        thursday = (CheckBox) root.findViewById(R.id.Thur_edit);
        friday = (CheckBox) root.findViewById(R.id.Fri_edit);
        saturday = (CheckBox) root.findViewById(R.id.Sat_edit);
        sunday = (CheckBox) root.findViewById(R.id.Sun_edit);


       imageView = root.findViewById(R.id.imageView_edit_alarm);


        db = new AlarmDatabase(getActivity());
        db.openReadable();



        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String result = bundle.getString("key");
                Cursor cursor = db.getData("SELECT * FROM Alarm where description = \""+result+"\";");

                String image = null;

                int hour = 0;
                int min = 0;
                String description = null;

                boolean recurring = false;
                boolean mon = false;
                boolean tue = false;
                boolean wed = false;
                boolean thur = false;
                boolean fri = false;
                boolean sat = false;
                boolean sun = false;
                while (cursor.moveToNext()) {

                    id[0] = cursor.getInt(0);
                    hour= cursor.getInt(1);
                    min = cursor.getInt(2);;
                    description = cursor.getString(3);;


                    if(cursor.getInt(4) == 1){
                        alarmOn[0] = true;
                    }

                    if(cursor.getInt(5) == 1){
                        recurring = true;
                    }

                    if(cursor.getInt(6) == 1){
                        mon = true;
                    }

                    if(cursor.getInt(7) == 1){
                        tue = true;
                    }

                    if(cursor.getInt(8) == 1){
                        wed = true;
                    }

                    if(cursor.getInt(9) == 1){
                        thur = true;
                    }

                    if(cursor.getInt(10) == 1){
                        fri = true;
                    }

                    if(cursor.getInt(11) == 1){
                        sat = true;
                    }

                    if(cursor.getInt(12) == 1){
                        sun = true;
                    }

                    image = cursor.getString(13);
                }

                picker.setHour(hour);
                picker.setMinute(min);
                des.setText(description);
                repeat.setChecked(recurring);
                monday.setChecked(mon);
                tuesday.setChecked(tue);
                wednesday.setChecked(wed);
                thursday.setChecked(thur);
                friday.setChecked(fri);
                saturday.setChecked(sat);
                sunday.setChecked(sun);

                byte[] imageByte = Base64.decode(image,Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                imageView.setImageBitmap(bitmap);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    //permission not granted, request it.
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    //show popup for runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    //permission already granted
                    pickImageFromGallery();
                }
            }
        });



        Button delete = (Button) root.findViewById(R.id.deleteAlarm_button);

        delete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                db.deleteData(id[0]);
                Toast.makeText(getActivity(), "Success delete row", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(fragment_editAlarm.this)
                        .navigate(R.id.action_fragment_editAlarm_to_nav_alarm);
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
            edit();
            NavHostFragment.findNavController(fragment_editAlarm.this)
                    .navigate(R.id.action_fragment_editAlarm_to_nav_alarm);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void edit(){
        int on = 0;
        if(alarmOn[0]){
            on =1;
        }
        int recurring = 0;
        if(repeat.isChecked()){
            recurring= 1;
        }

        int mon = 0;
        if(monday.isChecked()){
            mon = 1;
        }

        int tue = 0;
        if(tuesday.isChecked()){
            tue = 1;
        }

        int wed = 0;
        if(wednesday.isChecked()){
            wed = 1;
        }

        int thur = 0;
        if(thursday.isChecked()){
            thur = 1;
        }

        int fri = 0;
        if(friday.isChecked()){
            fri = 1;
        }

        int sat = 0;
        if(saturday.isChecked()){
            sat = 1;
        }

        int sun = 0;
        if(sunday.isChecked()){
            sun = 1;
        }

        db.updateData(id[0],picker.getHour(),picker.getMinute(),des.getText().toString(),on,recurring,mon,tue,wed,thur,fri,sat,sun, Base64.encodeToString(imageViewToByte(imageView),Base64.DEFAULT));
        Toast.makeText(getActivity(), "Success update row", Toast.LENGTH_SHORT).show();


    }






    private static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

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
}
