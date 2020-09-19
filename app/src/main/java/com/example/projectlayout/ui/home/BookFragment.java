package com.example.projectlayout.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.projectlayout.R;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer2;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BookFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookFragment newInstance(String param1, String param2) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

        ViewPager2 book = root.findViewById(R.id.bookView);
        bookRecyclerAdapter bookRecyclerAdapter = new bookRecyclerAdapter(bookImage);
        book.setAdapter(bookRecyclerAdapter);

        BookFlipPageTransformer2 bookFlipPageTransformer = new BookFlipPageTransformer2();
        bookFlipPageTransformer.setEnableScale(true);
        bookFlipPageTransformer.setScaleAmountPercent(10f);
        book.setPageTransformer(bookFlipPageTransformer);


       return root;
    }
}
