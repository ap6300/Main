package com.example.projectlayout.ui.Wants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.example.projectlayout.R;

import java.util.ArrayList;
public class CustomAdapter extends ArrayAdapter{
    private ArrayList<want> dataSet;
    Context mContext;
    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        CheckBox checkBox;
    }
    public CustomAdapter(ArrayList<want> data, Context context) {
        super(context, R.layout.fragment_wants, data);
        this.dataSet = data;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return dataSet.size();
    }
    @Override
    public Object getItem(int position) {
        return dataSet.get(position);
    }
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.label);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        want item = dataSet.get(position);
        viewHolder.txtName.setText(item.name);

        if(item.checked == 1){
            viewHolder.checkBox.setChecked(true);
        } else {

            viewHolder.checkBox.setChecked(false);
        }
        return result;
    }
}