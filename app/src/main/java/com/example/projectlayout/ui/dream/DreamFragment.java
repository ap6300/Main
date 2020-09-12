package com.example.projectlayout.ui.dream;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.ArrayList;

public class DreamFragment extends Fragment  {

    public static DreamFragment newInstance() {
        return new DreamFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dream, container, false);
        setHasOptionsMenu(true);

        //ListView
        GridView gridview = (GridView) root.findViewById(R.id.gridview);

        DreamDatabase db = new DreamDatabase(getActivity());
        db.openReadable();

        ArrayList<Dreamboard> list = new ArrayList<>();
        ImageAdapter arrayAdpt = new ImageAdapter(getActivity(), getContext(), list);
        gridview.setAdapter(arrayAdpt);

        Cursor cursor = db.getData("SELECT * FROM Dreamboard");
        list.clear();
        while (cursor.moveToNext()) {
            String price = cursor.getString(0);
            byte[] image = cursor.getBlob(1);

            list.add(new Dreamboard( price, image));
        }

        arrayAdpt.notifyDataSetChanged();




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
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_photo) {
            NavHostFragment.findNavController(DreamFragment.this)
                    .navigate(R.id.action_nav_dream_to_fragment_addDream);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}






