package com.example.projectlayout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.projectlayout.ui.Alarm.testActivity;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        Intent intent1 = new Intent(context, testActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("STR_CALLER","");

        intent1.putExtras(bundle);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent alarmService = new Intent(context,AlarmService.class);
        context.startService(alarmService);

        //context.startActivity(intent1);

        //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();


    }
}
