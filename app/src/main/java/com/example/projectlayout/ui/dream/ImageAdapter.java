package com.example.projectlayout.ui.dream;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.projectlayout.R;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Dreamboard> list;

    // Constructor
    public ImageAdapter(FragmentActivity activity, Context c, ArrayList<Dreamboard> l) {
        this.mContext = c;
        this.list = l;

    }


    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imageView;
        TextView description;
    }

     */

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;
        //ViewHolder holder = new ViewHolder();
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.custom_listview_dreamboard, parent, false);
        }

        // still testing
        /*        Dreamboard dreamboard = list.get(position);

        holder.description.setText(dreamboard.getName());

        byte[] Image = dreamboard.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(Image, 0, Image.length);
        holder.imageView.setImageBitmap(bitmap);

        */
        //String currentString = titles.get(position);
        String currentString = "Hi";
        //Setting the image color
        ImageView imageView = listItem.findViewById(R.id.imageView_listView);

       // Map<String, String> htmlStandardColorMap = ColorUtil.getHtmlStandardColorMap();
        //if (htmlStandardColorMap.containsKey(currentString)) {
        //Toast.makeText(context, "Contains Color!", Toast.LENGTH_SHORT).show();
//            imageView.setBackgroundColor(Color.parseColor(htmlStandardColorMap.get(currentString)));
        //imageView.setBackgroundColor(Color.parseColor("#4f34eb"));
        //}
        //TODO - if not in the map do an API call to color API.

        TextView value = listItem.findViewById(R.id.textView_listView);
        value.setText(currentString);

        //imageView.setLayoutParams(new GridView.LayoutParams(200, 400));
        //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView.setPadding(8, 8, 8, 8);

        imageView.setImageResource(mThumbIds[position]);
        return listItem;
    }

    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.book_cover,R.drawable.book_cover,R.drawable.book_cover

    };
}
