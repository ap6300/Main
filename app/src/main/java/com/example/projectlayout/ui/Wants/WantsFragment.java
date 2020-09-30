package com.example.projectlayout.ui.Wants;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectlayout.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;

public class WantsFragment extends Fragment {


    private DontWantDatabase dontWantdb;
    private WantDatabase wantdb;
    private RecyclerView productRec;
    private ArrayList<want> item = new ArrayList<>();
    private ArrayList<want> item1 = new ArrayList<>();
    private TabLayout tab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_wants, container, false);
        setHasOptionsMenu(true);

        //init
        productRec = root.findViewById(R.id.prodrec);
        tab = root.findViewById(R.id.tabLayout);
        tab.setTabGravity(TabLayout.GRAVITY_FILL);

        //set up database
        dontWantdb = new DontWantDatabase(getActivity());
        dontWantdb.openReadable();

        wantdb = new WantDatabase(getActivity());
        wantdb.openReadable();

        item.clear();
        item = dontWantdb.getDontWantItem();

        //set up the adapter
        setup(item);

        //drag and drop
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        productRec.addItemDecoration(dividerItemDecoration);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(productRec);

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==1){
                    item1.clear();
                    item1 = wantdb.getWantItem();
                    //set up the adapter
                    setup(item1);

                } else if(tab.getPosition()==0) {
                    item.clear();
                    item = dontWantdb.getDontWantItem();
                    //set up the adapter
                    setup(item);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getPosition()==1){
                    item1.clear();
                    item1 = wantdb.getWantItem();
                    //set up the adapter
                    setup(item1);

                } else if(tab.getPosition()==0) {
                    item.clear();
                    item = dontWantdb.getDontWantItem();
                    //set up the adapter
                    setup(item);
                }

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

        if (id == R.id.add_toolbar) {
            showAdd();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            if(tab.getSelectedTabPosition()==1) {
                String x = wantdb.getListOrder(fromPosition);
                String y = wantdb.getListOrder(toPosition);
                Collections.swap(item1, fromPosition, toPosition);

                wantdb.updateListOrder(x, toPosition);
                wantdb.updateListOrder(y, fromPosition);
            }else {
                String x = dontWantdb.getListOrder(fromPosition);
                String y = dontWantdb.getListOrder(toPosition);
                Collections.swap(item, fromPosition, toPosition);

                dontWantdb.updateListOrder(x, toPosition);
                dontWantdb.updateListOrder(y, fromPosition);
            }

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            if(tab.getSelectedTabPosition()==1) {
                item1 = wantdb.getWantItem();
                setup(item1);
            }else{
                item = dontWantdb.getDontWantItem();
                setup(item);
            }
        }
    };

    private void setup(ArrayList<want> list){
        DontWantRecyclerViewAdapter adt = new DontWantRecyclerViewAdapter(list, getContext(), new DontWantRecyclerViewAdapter.RecycleViewInterface() {
            @Override
            public void onItemClick(int position) {
                showEdit(position);
            }
        });
        productRec.setLayoutManager(new LinearLayoutManager(getContext()));
        productRec.setAdapter(adt);
        adt.notifyDataSetChanged();
    }


    //Build alert dialog for adding task with an add button
    private void showAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if(tab.getSelectedTabPosition()==1) {
            builder.setTitle("Add Want");
        }else{
            builder.setTitle("Add Don't Want");
        }
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_add_item_first, null);
        builder.setView(customLayout);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editText = customLayout.findViewById(R.id.editTextTextPersonName);

                //determine the list
                if(tab.getSelectedTabPosition()==1) {
                    wantdb.openReadable();
                    int x = item1.size();
                    boolean work = wantdb.addRow(editText.getText().toString(), x);
                    wantdb.close();
                    if (work) {

                        item1 = wantdb.getWantItem();
                        //set up the adapter
                        setup(item1);
                    } else {
                        Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    dontWantdb.openReadable();
                    int x = item.size();
                    boolean work = dontWantdb.addRow(editText.getText().toString(), x);
                    dontWantdb.close();
                    if (work) {

                        item = dontWantdb.getDontWantItem();
                        //set up the adapter
                        setup(item);

                    } else {
                        Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showEdit(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_add_item_first, null);
        final EditText editText = customLayout.findViewById(R.id.editTextTextPersonName);
        String task= "";

        if(tab.getSelectedTabPosition()==1) {
            builder.setTitle("Edit Want");
            //fetch data from database
            Cursor cursor = wantdb.getData("SELECT * FROM Want where listOrder = \""+position+"\";");
            cursor.moveToNext();
            task = cursor.getString(0);
            cursor.close();

            //set text into edittext
            editText.setText(task);
        }else{
            builder.setTitle("Edit Don't Want");
            //fetch data from database
            Cursor cursor = dontWantdb.getData("SELECT * FROM DontWant where listOrder = \""+position+"\";");
            cursor.moveToNext();
            task = cursor.getString(0);
            cursor.close();

            //set text into the edittext
            editText.setText(task);
        }

        final String finalTask = task;

        builder.setView(customLayout);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(tab.getSelectedTabPosition()==1) {
                    wantdb.openReadable();
                    wantdb.update(finalTask,editText.getText().toString());

                    item1 = wantdb.getWantItem();
                    //set up the adapter
                    setup(item1);

                    wantdb.close();
                } else{
                    dontWantdb.openReadable();
                    dontWantdb.update(finalTask,editText.getText().toString());

                    item = dontWantdb.getDontWantItem();
                    //set up the adapter
                    setup(item);

                    dontWantdb.close();
                }
            }
        });
        builder.setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(tab.getSelectedTabPosition()==1) {
                            wantdb.clearRecords(finalTask);

                            for(int i=position;i<wantdb.count();i++) {
                                String y =wantdb.getListOrder(i+1);
                                wantdb.updateListOrder(y, i);
                            }

                            item1 = wantdb.getWantItem();
                            //set up the adapter
                            setup(item1);


                            wantdb.close();
                        } else {

                            dontWantdb.clearRecords(finalTask);
                            for(int i=position;i<dontWantdb.count();i++) {
                                String y =dontWantdb.getListOrder(i+1);
                                dontWantdb.updateListOrder(y, i);
                            }



                           item = dontWantdb.getDontWantItem();
                            //set up the adapter
                            setup(item);

                            dontWantdb.close();

                        }
                    }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }




}
