package com.example.projectlayout.ui.Alarm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.projectlayout.R;

import java.util.ArrayList;

public class AlarmAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Alarm> list;
    private alarmInterface alarmInterface;
    public interface alarmInterface {
        void onToggle(Alarm alarm);
    }

    public AlarmAdapter(Context c, ArrayList<Alarm> l,alarmInterface alarmInterface) {
        this.mContext = c;
        this.list = l;
        this.alarmInterface = alarmInterface;
    }

    private static class ViewHolder{
        TextView alarmTime;
        ImageView alarmRecurring;
        TextView alarmRecurringDays;
        TextView alarmTitle;
        Switch alarmStarted;
        ImageView imageView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;
        AlarmAdapter.ViewHolder holder = new AlarmAdapter.ViewHolder();

        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.custom_recyclerview_alarm, parent, false);

            final Alarm alarm = list.get(position);
            //init
            TextView alarmTime = listItem.findViewById(R.id.item_alarm_time);
            Switch alarmStarted = listItem.findViewById(R.id.item_alarm_started);
            ImageView alarmRecurring = listItem.findViewById(R.id.item_alarm_recurring);
            TextView alarmRecurringDays = listItem.findViewById(R.id.item_alarm_recurringDays);
            TextView alarmTitle = listItem.findViewById(R.id.item_alarm_title);
            ImageView imageView = listItem.findViewById(R.id.imageView2);

            //set the custom view value
            holder.alarmTime = alarmTime;
            holder.alarmStarted = alarmStarted;
            holder.alarmRecurring = alarmRecurring;
            holder.alarmRecurringDays = alarmRecurringDays;
            holder.alarmTitle = alarmTitle;
            holder.imageView = imageView;

            @SuppressLint("DefaultLocale") String alarmText = String.format("%02d:%02d", alarm.getHour(), alarm.getMin());
            holder.alarmTime.setText(alarmText);

            if (alarm.isAlarmOn()) {
                holder.alarmStarted.setChecked(true);
            }

            if (alarm.isRecurring()) {
                holder.alarmRecurring.setImageResource(R.drawable.ic_repeat_black_24dp);
                holder.alarmRecurringDays.setText("Recurring");
            } else {
                holder.alarmRecurring.setImageResource(R.drawable.ic_looks_one_black_24dp);
                holder.alarmRecurringDays.setText("once Off");
            }

            holder.alarmTitle.setText(alarm.getDescription());

            byte[] recordImage = Base64.decode(alarm.getImage(),Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);
            holder.imageView.setImageBitmap(bitmap);

            alarmStarted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    alarmInterface.onToggle(alarm);
                }
            });

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
