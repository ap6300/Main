package com.example.projectlayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.example.projectlayout.ui.Alarm.Alarm.DESCRIPTION;
import static com.example.projectlayout.ui.Alarm.Alarm.ID;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        /*
        Intent intent1 = new Intent(context, testActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("STR_CALLER","");

        intent1.putExtras(bundle);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        */
        Intent alarmService = new Intent(context,AlarmService.class);
        alarmService.putExtra(DESCRIPTION, intent.getStringExtra(DESCRIPTION));
        alarmService.putExtra(ID,intent.getIntExtra(ID,123));
        //alarmService.putExtra(IMAGE,intent.getByteArrayExtra(IMAGE));
        context.startService(alarmService);

        //context.startActivity(intent1);

        //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();


    }
}
