package com.example.projectlayout.ui.Wants;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectlayout.R;


/**
 * Created by hanh.vo on 12/04/2018.
 */

public class CustomAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private boolean checked = false;

    // boolean array for storing
    //the state of each CheckBox
    boolean[] checkBoxState;


    ViewHolder viewHolder;


    public CustomAdapter(Context context, String[] values) {
        super(context, R.layout.rowlayout, values);
        this.context = context;
        this.values = values;
        checkBoxState = new boolean[values.length];
    }

    private class ViewHolder
    {
        ImageView photo;
        TextView name;
        CheckBox checkBox;

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public TextView getName() {
            return name;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.rowlayout, null);
            viewHolder=new ViewHolder();

            //cache the views
            viewHolder.photo=(ImageView) convertView.findViewById(R.id.icon);
            viewHolder.name=(TextView) convertView.findViewById(R.id.label);
            viewHolder.checkBox=(CheckBox) convertView.findViewById(R.id.checkBox);

            //link the cached views to the convertview
            convertView.setTag( viewHolder);


        }
        else
            viewHolder=(ViewHolder) convertView.getTag();

        viewHolder.name.setText(values[position]);



        String s = values[position];

        //set icon image

        if (s.startsWith("Windows7") || s.startsWith("iPhone") || s.startsWith("Solaris")) {
            viewHolder.photo.setImageResource(R.drawable.logo);
        } else {
            viewHolder.photo.setImageResource(R.drawable.money);
        }


        //VITAL PART!!! Set the state of the
        //CheckBox using the boolean array
        viewHolder.checkBox.setChecked(checkBoxState[position]);


        //for managing the state of the boolean
        //array according to the state of the
        //CheckBox

        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(((CheckBox)v).isChecked()) {
                    checkBoxState[position] = true;
                }
                else
                    checkBoxState[position]=false;

            }
        });

        //return the view to be displayed
        return convertView;
    }

    public boolean[] getCheckBoxState(){
        return checkBoxState;
    }

    public String getName(int pos){
        String val = values[pos];
        return val;
    }


}


