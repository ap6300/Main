package com.example.projectlayout.ui.Wants;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.projectlayout.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class WantsFragment extends Fragment {

    private WantsViewModel galleryViewModel;
    private WantsDatabasee mydatabase;
    private TextView response;
    private ListView productRec;
    private EditText phone, name, address, gender;
    private Button addButton;
    private TableLayout addLayout;
    private boolean Inserted;
    private CustomAdapter adt;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(WantsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_wants, container, false);
        setHasOptionsMenu(true);

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton2);

        productRec = (ListView)root.findViewById(R.id.prodrec);

        mydatabase = new WantsDatabasee(getActivity());

        mydatabase.openReadable();
        mydatabase.clear();

        ArrayList<want> item = new ArrayList<>();

        adt = new CustomAdapter(item,getContext());
        productRec.setAdapter(adt);

        Cursor cursor = mydatabase.getData("SELECT * FROM Task");
        item.clear();
        while (cursor.moveToNext()) {
            String task = cursor.getString(0);
            int checked = cursor.getInt(1);

            item.add(new want( task, checked));
        }
        adt.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showAlertDialogButtonClicked(getView());
                adt.notifyDataSetChanged();

            }
        });




        return root;
    }



    public void showAlertDialogButtonClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Desire");
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_add_item_first, null);
        builder.setView(customLayout);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = customLayout.findViewById(R.id.editTextTextPersonName);

                mydatabase.openReadable();
                Boolean work = mydatabase.addRow(editText.getText().toString(),0);
                if(work){
                    sendDialogDataToActivity(editText.getText().toString());
                }else{
                    Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                }
                ArrayList<want> item = new ArrayList<>();

                adt = new CustomAdapter(item,getContext());
                productRec.setAdapter(adt);

                Cursor cursor = mydatabase.getData("SELECT * FROM Task");
                item.clear();
                while (cursor.moveToNext()) {
                    String task = cursor.getString(0);
                    int checked = cursor.getInt(1);

                    item.add(new want( task, checked));
                }

                adt.notifyDataSetChanged();

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    private void sendDialogDataToActivity(String data) {
        Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();

    }


}
