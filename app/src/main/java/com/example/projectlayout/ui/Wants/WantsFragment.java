package com.example.projectlayout.ui.Wants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.projectlayout.R;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class WantsFragment extends Fragment {

    private WantsViewModel galleryViewModel;

    private DatabaseManager mydatabase;
    private TextView response;
    private ListView productRec;
    private EditText phone, name, address, gender;
    private Button addButton;
    private TableLayout addLayout;
    private boolean Inserted;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(WantsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_wants, container, false);
        mydatabase = new DatabaseManager(getActivity());
        response = (TextView)root.findViewById(R.id.response);
        productRec = (ListView)root.findViewById(R.id.prodrec);
        addLayout = (TableLayout)root.findViewById(R.id.add_table);

        //want add fragment
        addButton = (Button)root.findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Inserted = mydatabase.addRow("hi");
                addLayout.setVisibility(View.GONE);
                if (Inserted) {
                    response.setText("The row in the table is inserted");
                }
                else {
                    response.setText("Sorry, errors when inserting to DB");
                }
            }
        });
        return root;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.add:
                //Addlists();
                break;
            case R.id.show:
                //Showlists();
                break;
            case R.id.delete:
                //Removelists();
                break;
        }
        //return true;
        return super.onOptionsItemSelected(item);
    }
    public boolean Showlists() {

        mydatabase.openReadable();
        ArrayList<String> tableContent = mydatabase.retrieveRows();
        response.setText("The list: \n");
        ArrayAdapter<String> arrayAdpt = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, tableContent);
        productRec.setAdapter(arrayAdpt);
        return true;
    }

    public boolean Addlists() {
        addLayout.setVisibility(View.VISIBLE);
        response.setText("Enter information of a new task");
        productRec.setVisibility(View.GONE);
        return true;
    }

    public boolean Removelists() {
        mydatabase.clearRecords("hi");
        response.setText("List removed");
        productRec.setAdapter(null);
        return true;
    }





}
