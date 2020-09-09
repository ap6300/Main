package com.example.projectlayout.ui.dream;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projectlayout.R;

import java.util.ArrayList;

public class DreamFragment extends Fragment  {

    private ImageAdapter mAdapter;
    private DreamDatabase db;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    private DreamViewModel mViewModel;


    ImageView mImageView;
    private boolean recInserted;
    private EditText description;
    private ArrayList<Dreamboard> list;

    public static DreamFragment newInstance() {
        return new DreamFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dream, container, false);
        setHasOptionsMenu(true);
        //mydManager = new DreamDatabase(getActivity());
        //ListView
        ListView gridview = (ListView) root.findViewById(R.id.gridview);

        db = new DreamDatabase( getActivity());
        db.openReadable();


        mAdapter = new ImageAdapter(getActivity(),getContext(),list);
        gridview.setAdapter(mAdapter);



        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DreamViewModel.class);
        // TODO: Use the ViewModel


    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
            Toast.makeText(getActivity(), "Change", Toast.LENGTH_LONG).show();
            NavHostFragment.findNavController(DreamFragment.this)
                    .navigate(R.id.action_nav_dream_to_fragment_addDream);
            return true;
        }
//
//

        return super.onOptionsItemSelected(item);
    }


}






