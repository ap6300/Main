package com.example.projectlayout.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.projectlayout.R;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer2;

public class BookFragment extends Fragment {


    public BookFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View root = inflater.inflate(R.layout.fragment_book, container, false);
       int[] bookImage = new int[]{R.drawable.book1,R.drawable.book2,R.drawable.book3,R.drawable.book4,R.drawable.book5,R.drawable.book6,R.drawable.book7,R.drawable.book8,R.drawable.book9,R.drawable.book10,
                                R.drawable.book11,R.drawable.book12};


       //Initiate Viewpaper and  adapter
        ViewPager2 book = root.findViewById(R.id.bookView);
        bookRecyclerAdapter bookRecyclerAdapter = new bookRecyclerAdapter(bookImage);

        //set adapter for viewpapaer
        book.setAdapter(bookRecyclerAdapter);

        //set up the flip page transformation
        BookFlipPageTransformer2 bookFlipPageTransformer = new BookFlipPageTransformer2();
        bookFlipPageTransformer.setEnableScale(true);
        bookFlipPageTransformer.setScaleAmountPercent(10f);
        book.setPageTransformer(bookFlipPageTransformer);


       return root;
    }
}
