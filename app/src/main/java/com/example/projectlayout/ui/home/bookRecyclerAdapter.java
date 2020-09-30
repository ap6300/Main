package com.example.projectlayout.ui.home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectlayout.R;

public class bookRecyclerAdapter extends RecyclerView.Adapter<bookRecyclerAdapter.ViewHolder> {
    private final int[] mValues;
    //constructor
    bookRecyclerAdapter(int[] items) {
        this.mValues = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //set the image
        holder.mImageView.setImageResource(mValues[position]);

        //Set the background to be black for the book cover else be white background
        if(mValues[position] == (R.drawable.book1)) {
            holder.mImageView.setBackgroundColor(Color.BLACK);
        }else{
            holder.mImageView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView mImageView;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = view.findViewById(R.id.bookImg);

        }
    }
}






















   /* public bookRecyclerAdapter(int[] myDataset) {
       this.mDataset = myDataset;
    }


    @NonNull
    @Override
    public bookRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_book,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull bookRecyclerAdapter.MyViewHolder holder, int position) {
            holder.imageView.setImageResource(mDataset[position]);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public MyViewHolder(View v) {
            super(v);
            this.imageView = v.findViewById(R.id.bookImg);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

*/