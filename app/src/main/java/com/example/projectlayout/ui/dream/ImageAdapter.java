package com.example.projectlayout.ui.dream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectlayout.R;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Dreamboard> list;

    // Constructor
    public ImageAdapter( Context c, ArrayList<Dreamboard> l) {
        this.mContext = c;
        this.list = l;

    }
    private static class ViewHolder{
        ImageView imageView;
        TextView description;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;
        ViewHolder holder = new ViewHolder();

        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.custom_listview_dreamboard, parent, false);


            Dreamboard dreamboard = list.get(position);

            ImageView imageView = listItem.findViewById(R.id.imageView_listView);
            TextView textView = listItem.findViewById(R.id.textView_listView);

            holder.imageView = imageView;
            holder.description = textView;
            //set the custom view value
            textView.setText(dreamboard.getName());
            holder.description.setText(dreamboard.getName());

            byte[] recordImage = dreamboard.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);
            holder.imageView.setImageBitmap(bitmap);




        }

        return listItem;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
