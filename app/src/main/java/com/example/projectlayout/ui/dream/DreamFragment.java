package com.example.projectlayout.ui.dream;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.projectlayout.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class DreamFragment extends Fragment  {

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private ImageView mImageView;
    private EditText description;
    private boolean recInserted;
    private DreamDatabase db;
    private GridView gridview;

    public static DreamFragment newInstance() {
        return new DreamFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fetch data from database and set to gridview
        db = new DreamDatabase(getActivity());
        db.openReadable();

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dream, container, false);

        setHasOptionsMenu(true);
        //init
        gridview = root.findViewById(R.id.gridview);

        setUpAdapter();

        //set onclick to have alert dialog
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               TextView hi = view.findViewById(R.id.textView_listView);
               showEdit(getView(),hi.getText().toString());
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
        if (item.getItemId() == R.id.add_toolbar) {
            showAlertDialogAddClicked(getView());
        }
        return super.onOptionsItemSelected(item);
    }
    //set the adapter to the listview
    private void setUpAdapter(){
        ArrayList<Dreamboard> list = db.getDream();
        ImageAdapter arrayAdpt = new ImageAdapter(getContext(), list);
        gridview.setAdapter(arrayAdpt);
        db.close();
    }

    //show dialog when the add button pressed
    private void showAlertDialogAddClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add Dreamboard");
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_add_dreamboard, null);
        mImageView = customLayout.findViewById(R.id.image);
        description = customLayout.findViewById(R.id.description);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
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

        builder.setView(customLayout);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (description.getText() != null && (int) mImageView.getTag() == 1) {
                    recInserted = true;
                    db.openReadable();
                    recInserted = db.addRow(Objects.requireNonNull(description.getText()).toString(), imageViewToByte(mImageView));
                }
                setUpAdapter();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    //show dialog when the edit button pressed
    private void showEdit(View view,String selected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Dreamboard");
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_add_dreamboard, null);
        mImageView = customLayout.findViewById(R.id.image);
        description = customLayout.findViewById(R.id.description);

        Cursor cursor = db.getData("SELECT * FROM Dreamboard where description = \""+selected+"\";");

        cursor.moveToNext();
        final String price = cursor.getString(0);
        byte[] image = cursor.getBlob(1);

        cursor.close();

        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        description.setText(price);
        mImageView.setImageBitmap(bitmap);



        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
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

        builder.setView(customLayout);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.updateData(description.getText().toString(),imageViewToByte(mImageView), price);
                setUpAdapter();

            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.deleteData(price);
                setUpAdapter();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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






