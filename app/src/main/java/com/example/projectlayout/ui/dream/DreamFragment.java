package com.example.projectlayout.ui.dream;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectlayout.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class DreamFragment extends Fragment  {

    private ArrayList<Dreamboard> list = new ArrayList<>();
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private ImageView mImageView;
    private EditText description;
    private boolean recInserted;
    private DreamDatabase db;
    private ImageAdapter arrayAdpt;
    private GridView gridview;

    public static DreamFragment newInstance() {
        return new DreamFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dream, container, false);

        setHasOptionsMenu(true);



        //ListView
        gridview = root.findViewById(R.id.gridview);


        db = new DreamDatabase(getActivity());
        db.openReadable();

        setUpAdapter();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               Bundle result = new Bundle();
               TextView hi = view.findViewById(R.id.textView_listView);

               result.putString("key", (String) hi.getText());
               getParentFragmentManager().setFragmentResult("key", result);


               NavHostFragment.findNavController(DreamFragment.this).navigate(R.id.action_nav_dream_to_fragment_editDream);

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
                        recInserted = db.addRow(Objects.requireNonNull(description.getText()).toString(), imageViewToByte(mImageView));
                    }

                    if (recInserted) {
                        Toast.makeText(getActivity(), "Success add row", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), "Not Success add row", Toast.LENGTH_SHORT).show();
                    }

                    setUpAdapter();

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
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

    private void setUpAdapter(){
        list = db.getDream();
        arrayAdpt = new ImageAdapter(getContext(),list);
        gridview.setAdapter(arrayAdpt);
    }

}






