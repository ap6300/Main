package com.example.projectlayout.ui.Alarm;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
    private int[] id = {0};
    private boolean[] alarmOn = {false};
    private ImageView imageView;
    private int[] weekday = new int[7];
    private boolean[] fetch = new boolean[7];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         final View root = inflater.inflate(R.layout.fragment_edit_alarm, container, false);
         setHasOptionsMenu(true);


        picker=root.findViewById(R.id.timePicker1_edit);
        des = root.findViewById(R.id.description_edit_alarm);





       imageView = root.findViewById(R.id.imageView_edit_alarm);


        db = new AlarmDatabase(getActivity());
        db.openReadable();

        final String[] week = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        final String[] shortWeek = {"Mon", "Tue", "Wed", "Thur", "Fri", "Sat", "Sun"};
        final TextView show_recurring = root.findViewById(R.id.show_recurring1);
        final ArrayList<String> list = new ArrayList<>();
        final int[] i = {0};
        final int[] x = {0};

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
                        x[0]=1;
                    }
                    fetch[0]= cursor.getInt(6) == 1;
                    fetch[1]= cursor.getInt(7) == 1;
                    fetch[2]=cursor.getInt(8)== 1;
                    fetch[3]=cursor.getInt(9)== 1;
                    fetch[4]=cursor.getInt(10)== 1;
                    fetch[5]=cursor.getInt(11)== 1;
                    fetch[6]=cursor.getInt(12)== 1;
                    image = cursor.getString(13);
                }
                cursor.close();

                picker.setHour(hour);
                picker.setMinute(min);
                des.setText(description);
                repeat.setChecked(recurring);
                if (x[0]==1){
                    i[0] =1;
                    list.clear();
                    for(int i = 0 ; i < week.length; i ++){
                        if(fetch[i]){
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
                }else{
                    i[0]=1;
                    show_recurring.setText("");
                }
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

        repeat =  root.findViewById(R.id.repeat_edit);
        repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked & i[0] ==1) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Repeat");


                        builder.setMultiChoiceItems(week, fetch, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    weekday[which] = 1;
                                    fetch[which] =true;
                                } else {
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
                                for (int i = 0; i < week.length; i++) {
                                    if (fetch[i]) {
                                        list.add(shortWeek[i]);
                                    }
                                }

                                StringBuilder out = new StringBuilder();
                                for (int i = 0; i < list.size(); i++) {
                                    if (i != list.size() - 1) {
                                        out.append(list.get(i)).append(",");
                                    } else {
                                        out.append(list.get(i));
                                    }
                                }
                                show_recurring.setText(out);
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }else{
                        i[0]=1;
                        show_recurring.setText("");
                        for(int j =0; j < weekday.length ;j++) {
                            weekday[j] = 0;
                        }
                    }

            }
        });



        Button delete = root.findViewById(R.id.deleteAlarm_button);

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
        if(fetch[0]){
            weekday[0]=1;
        }
        if(fetch[1]){
            weekday[1]=1;
        }
        if(fetch[2]){
            weekday[2]=1;
        }
        if(fetch[3]){
            weekday[3]=1;
        }
        if(fetch[4]){
            weekday[4]=1;
        }
        if(fetch[5]){
            weekday[5]=1;
        }
        if(fetch[6]){
            weekday[6]=1;
        }



        db.updateData(id[0],picker.getHour(),picker.getMinute(),des.getText().toString(),on,recurring,weekday[0],weekday[1],weekday[2],weekday[3],weekday[4],weekday[5],weekday[6], Base64.encodeToString(imageViewToByte(imageView),Base64.DEFAULT));
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
