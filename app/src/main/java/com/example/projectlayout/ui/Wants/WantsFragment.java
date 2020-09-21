package com.example.projectlayout.ui.Wants;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projectlayout.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class WantsFragment extends Fragment {


    private DontWantDatabase dontWantdb;
    private WantDatabase wantdb;
    private ListView productRec;
    private CustomAdapter adt;
    private WantCustomAdapter adt1;
    private ArrayList<want> item = new ArrayList<>();
    private ArrayList<want> item1 = new ArrayList<>();
    private TabLayout tab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_wants, container, false);
        setHasOptionsMenu(true);

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton2);

        productRec = root.findViewById(R.id.prodrec);

        dontWantdb = new DontWantDatabase(getActivity());
        dontWantdb.openReadable();

        wantdb = new WantDatabase(getActivity());
        wantdb.openReadable();

        item.clear();
        item = dontWantdb.getDontWantItem();


        adt = new CustomAdapter(item,getContext());
        productRec.setAdapter(adt);


        tab = root.findViewById(R.id.tabLayout);

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==1){
                    item1.clear();
                    item1 = wantdb.getWantItem();
                    adt1 = new WantCustomAdapter(item1,getContext());
                    productRec.setAdapter(adt1);
                } else if(tab.getPosition()==0) {

                    item.clear();
                    item = dontWantdb.getDontWantItem();
                    adt = new CustomAdapter(item, getContext());
                    productRec.setAdapter(adt);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                showAlertDialogButtonClicked(getView());
            }
        });




        return root;
    }



    private void showAlertDialogButtonClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Desire");
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_add_item_first, null);
        builder.setView(customLayout);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = customLayout.findViewById(R.id.editTextTextPersonName);

                if(tab.getSelectedTabPosition()==1) {
                    wantdb.openReadable();
                    boolean work = wantdb.addRow(editText.getText().toString(), 0);
                    wantdb.close();
                    if (work) {
                        sendDialogDataToActivity(editText.getText().toString());
                    } else {
                        Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                    }
                    item1 = wantdb.getWantItem();
                    adt1 = new WantCustomAdapter(item1,getContext());
                    productRec.setAdapter(adt1);
                    adt1.notifyDataSetChanged();


                } else{
                    dontWantdb.openReadable();
                    boolean work = dontWantdb.addRow(editText.getText().toString(), 0);
                    dontWantdb.close();
                    if (work) {
                        sendDialogDataToActivity(editText.getText().toString());
                    } else {
                        Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                    }
                    item = dontWantdb.getDontWantItem();
                    adt = new CustomAdapter(item,getContext());
                    productRec.setAdapter(adt);
                    adt.notifyDataSetChanged();

                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    private void sendDialogDataToActivity(String data) {
        Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();

    }









}
