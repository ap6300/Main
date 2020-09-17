package com.example.projectlayout.ui.Alarm;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.projectlayout.AlarmService;
import com.example.projectlayout.R;

import static com.example.projectlayout.ui.Alarm.Alarm.DESCRIPTION;
import static com.example.projectlayout.ui.Alarm.Alarm.ID;

public class testActivity extends Activity {
    private MediaPlayer mediaPlayer;
    public static final String CHANNEL_ID = "ALARM_SERVICE_CHANNEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        String alarmTitle = getIntent().getStringExtra(DESCRIPTION);
        int notificationId = getIntent().getIntExtra(ID,0);

        TextView one = findViewById(R.id.textView3);
        TextView two = findViewById(R.id.textView6);

        one.setText(alarmTitle);
        two.setText(String.valueOf(notificationId));

        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentService = new Intent(getApplicationContext(), AlarmService.class);
                getApplicationContext().stopService(intentService);
                finish();
            }
        });

    }




}

