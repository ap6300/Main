package com.example.projectlayout.ui.Wants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.projectlayout.R;

import java.util.ArrayList;

public class WantCustomAdapter extends ArrayAdapter {
    private ArrayList<want> dataSet;
    private Context mContext;
    private static class ViewHolder {
        TextView txtName;
        CheckBox checkBox;
    }
    public WantCustomAdapter(ArrayList<want> data, Context context) {
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
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        WantCustomAdapter.ViewHolder viewHolder;
        final View result;
        if (convertView == null) {
            viewHolder = new WantCustomAdapter.ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.label);
            viewHolder.checkBox = convertView.findViewById(R.id.checkBox);
            result=convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (WantCustomAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }
        final want item = dataSet.get(position);
        viewHolder.txtName.setText(item.name);

        if(item.checked == 1){
            viewHolder.checkBox.setChecked(true);
        } else {

            viewHolder.checkBox.setChecked(false);
        }

        final WantDatabase mydatabase = new WantDatabase(getContext());

        mydatabase.openReadable();

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    mydatabase.updateIsChecked(item.name);
                }
                else
                {
                    mydatabase.updateIsNotChecked(item.name);
                }
            }});


        return result;
    }
}
