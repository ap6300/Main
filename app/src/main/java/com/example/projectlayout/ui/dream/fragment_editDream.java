package com.example.projectlayout.ui.dream;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.example.projectlayout.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_editDream#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_editDream extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    public fragment_editDream() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_editDream.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_editDream newInstance(String param1, String param2) {
        fragment_editDream fragment = new fragment_editDream();
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

    private DreamDatabase db;
    private ArrayList<Dreamboard> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View root = inflater.inflate(R.layout.fragment_edit_dream, container, false);

        db = new DreamDatabase(getActivity());
        db.openReadable();



        final EditText editText = root.findViewById(R.id.editText_editDream);
        final ImageView imageView = root.findViewById(R.id.imageView_editDream);
        final String[] hold = {""};
        getParentFragmentManager().setFragmentResultListener("key", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                String result = bundle.getString("key");
                Cursor cursor = db.getData("SELECT * FROM Dreamboard where description = \""+result+"\";");
                String price= "";
                byte[] image = null;
                while (cursor.moveToNext()) {
                    price = cursor.getString(0);
                    image = cursor.getBlob(1);
                }
                hold[0] = price;
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                editText.setText(price);
                imageView.setImageBitmap(bitmap);
            }
        });




        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not granted, request it.
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        //show popup for runtime permission
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        //permission already granted
                        pickImageFromGallery();
                    }
                } else {
                    //system os is less then marshmallow
                    pickImageFromGallery();
                }
            }
        });


        Button update = (Button) root.findViewById(R.id.update_button);

        update.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                db.updateData(editText.getText().toString(),imageViewToByte(imageView), hold[0]);
                Toast.makeText(getActivity(), "Success update row", Toast.LENGTH_SHORT).show();
            }
        });

        Button delete = (Button) root.findViewById(R.id.delete_button);

        delete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                db.deleteData(hold[0]);
                Toast.makeText(getActivity(), "Success delete row", Toast.LENGTH_SHORT).show();
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

    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    //handle result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
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

}
