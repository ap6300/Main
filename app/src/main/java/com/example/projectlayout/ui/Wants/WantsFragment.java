package com.example.projectlayout.ui.Wants;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;

public class WantsFragment extends Fragment {


    private DontWantDatabase dontWantdb;
    private WantDatabase wantdb;
    private RecyclerView productRec;
    private WantCustomAdapter adt1;
    private DontWantRecyclerViewAdapter adt;
    private ArrayList<want> item = new ArrayList<>();
    private ArrayList<want> item1 = new ArrayList<>();
    private TabLayout tab;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_wants, container, false);
        setHasOptionsMenu(true);

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton2);

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
        adt = new DontWantRecyclerViewAdapter(item, getContext(), new DontWantRecyclerViewAdapter.RecycleViewInterface() {
            @Override
            public void onItemClick(int position) {
                showEdit(getView(),position);
            }
        });
        productRec.setLayoutManager(new LinearLayoutManager(getContext()));
        productRec.setAdapter(adt);
        adt.notifyDataSetChanged();

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
                    adt = new DontWantRecyclerViewAdapter(item1,getContext(), new DontWantRecyclerViewAdapter.RecycleViewInterface() {
                        @Override
                        public void onItemClick(int position) {

                        }
                    });
                    productRec.setLayoutManager(new LinearLayoutManager(getContext()));
                    productRec.setAdapter(adt);
                    adt.notifyDataSetChanged();

                } else if(tab.getPosition()==0) {
                    item.clear();
                    item = dontWantdb.getDontWantItem();
                    adt = new DontWantRecyclerViewAdapter(item,getContext(), new DontWantRecyclerViewAdapter.RecycleViewInterface() {
                        @Override
                        public void onItemClick(int position) {

                        }
                    });
                    productRec.setLayoutManager(new LinearLayoutManager(getContext()));
                    productRec.setAdapter(adt);
                    adt.notifyDataSetChanged();
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
                    adt = new DontWantRecyclerViewAdapter(item1,getContext(), new DontWantRecyclerViewAdapter.RecycleViewInterface() {
                        @Override
                        public void onItemClick(int position) {

                        }
                    });
                    productRec.setLayoutManager(new LinearLayoutManager(getContext()));
                    productRec.setAdapter(adt);
                    adt.notifyDataSetChanged();

                } else if(tab.getPosition()==0) {
                    item.clear();
                    item = dontWantdb.getDontWantItem();
                    adt = new DontWantRecyclerViewAdapter(item,getContext(), new DontWantRecyclerViewAdapter.RecycleViewInterface() {
                        @Override
                        public void onItemClick(int position) {

                        }
                    });
                    productRec.setLayoutManager(new LinearLayoutManager(getContext()));
                    productRec.setAdapter(adt);
                    adt.notifyDataSetChanged();
                }

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
    };



    private void showAlertDialogButtonClicked(View view) {
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

                if(tab.getSelectedTabPosition()==1) {
                    wantdb.openReadable();
                    int x = item1.size();
                    boolean work = wantdb.addRow(editText.getText().toString(), x);
                    wantdb.close();
                    if (work) {
                        sendDialogDataToActivity(editText.getText().toString());
                        item1 = wantdb.getWantItem();
                        adt = new DontWantRecyclerViewAdapter(item1,getContext(), new DontWantRecyclerViewAdapter.RecycleViewInterface() {
                            @Override
                            public void onItemClick(int position) {

                            }
                        });
                        productRec.setAdapter(adt);
                        productRec.setLayoutManager(new LinearLayoutManager(getContext()));
                        adt.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    dontWantdb.openReadable();
                    int x = item.size();
                    boolean work = dontWantdb.addRow(editText.getText().toString(), x);
                    dontWantdb.close();
                    if (work) {
                        sendDialogDataToActivity(editText.getText().toString());
                        item = dontWantdb.getDontWantItem();
                        adt = new DontWantRecyclerViewAdapter(item,getContext(), new DontWantRecyclerViewAdapter.RecycleViewInterface() {
                            @Override
                            public void onItemClick(int position) {

                            }
                        });
                        productRec.setAdapter(adt);
                        productRec.setLayoutManager(new LinearLayoutManager(getContext()));
                        adt.notifyDataSetChanged();

                    } else {
                        Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    private void sendDialogDataToActivity(String data) {
        Toast.makeText(getActivity(), data, Toast.LENGTH_SHORT).show();

    }


    private void showEdit(View view, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        if(tab.getSelectedTabPosition()==1) {
            builder.setTitle("Edit Want");
        }else{
            builder.setTitle("Edit Don't Want");
        }
        final View customLayout = getLayoutInflater().inflate(R.layout.custom_add_item_first, null);
        builder.setView(customLayout);
        final EditText editText = customLayout.findViewById(R.id.editTextTextPersonName);

        Cursor cursor = dontWantdb.getData("SELECT * FROM DontWant where listOrder = \""+position+"\";");
        String task= "";

        cursor.moveToNext();
        task = cursor.getString(0);

        editText.setText(task);


        final String finalTask = task;
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if(tab.getSelectedTabPosition()==1) {
                    wantdb.openReadable();
                    int x = item1.size();
                    boolean work = wantdb.addRow(editText.getText().toString(), x);
                    wantdb.close();
                    if (work) {
                        sendDialogDataToActivity(editText.getText().toString());
                        item1 = wantdb.getWantItem();
                        adt = new DontWantRecyclerViewAdapter(item1,getContext(), new DontWantRecyclerViewAdapter.RecycleViewInterface() {
                            @Override
                            public void onItemClick(int position) {

                            }
                        });
                        productRec.setAdapter(adt);
                        productRec.setLayoutManager(new LinearLayoutManager(getContext()));
                        adt.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    dontWantdb.openReadable();

                    dontWantdb.update(finalTask,editText.getText().toString());


                        sendDialogDataToActivity(editText.getText().toString());
                        item = dontWantdb.getDontWantItem();
                        adt = new DontWantRecyclerViewAdapter(item,getContext(), new DontWantRecyclerViewAdapter.RecycleViewInterface() {
                            @Override
                            public void onItemClick(int position) {

                            }
                        });
                        productRec.setAdapter(adt);
                        productRec.setLayoutManager(new LinearLayoutManager(getContext()));
                        adt.notifyDataSetChanged();

                    dontWantdb.close();
                }
            }
        });
        builder.setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dontWantdb.clearRecords(editText.getText().toString());
                        item = dontWantdb.getDontWantItem();
                        adt = new DontWantRecyclerViewAdapter(item,getContext(), new DontWantRecyclerViewAdapter.RecycleViewInterface() {
                            @Override
                            public void onItemClick(int position) {

                            }
                        });
                        productRec.setAdapter(adt);
                        productRec.setLayoutManager(new LinearLayoutManager(getContext()));
                        adt.notifyDataSetChanged();


                    }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }



}
