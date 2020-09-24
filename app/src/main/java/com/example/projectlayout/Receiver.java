package com.example.projectlayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.example.projectlayout.ui.Alarm.Alarm.DESCRIPTION;
import static com.example.projectlayout.ui.Alarm.Alarm.ID;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

            Intent alarmService = new Intent(context, AlarmService.class);
            alarmService.putExtra(DESCRIPTION, intent.getStringExtra(DESCRIPTION));
            alarmService.putExtra(ID, intent.getIntExtra(ID, 0));
            context.startService(alarmService);
    }
}
