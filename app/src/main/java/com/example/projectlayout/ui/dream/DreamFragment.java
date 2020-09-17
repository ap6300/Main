package com.example.projectlayout.ui.dream;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectlayout.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DreamFragment extends Fragment  {

    public static DreamFragment newInstance() {
        return new DreamFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dream, container, false);


        //ListView
        GridView gridview = (GridView) root.findViewById(R.id.gridview);

        FloatingActionButton fab = root.findViewById(R.id.floatingActionButton3);

        DreamDatabase db = new DreamDatabase(getActivity());
        db.openReadable();

        ArrayList<Dreamboard> list = new ArrayList<>();
        ImageAdapter arrayAdpt = new ImageAdapter(getContext(), list);
        gridview.setAdapter(arrayAdpt);

        Cursor cursor = db.getData("SELECT * FROM Dreamboard");
        list.clear();
        while (cursor.moveToNext()) {
            String price = cursor.getString(0);
            byte[] image = cursor.getBlob(1);

            list.add(new Dreamboard( price, image));
        }



        arrayAdpt.notifyDataSetChanged();

        db.close();


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

        fab.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(DreamFragment.this)
                        .navigate(R.id.action_nav_dream_to_fragment_addDream);

            }
        });

        return root;
    }








}






