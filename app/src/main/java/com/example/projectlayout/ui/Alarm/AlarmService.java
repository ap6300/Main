package com.example.projectlayout.ui.Alarm;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.projectlayout.R;

import static com.example.projectlayout.ui.Alarm.AlarmBroadcastReceiver.DESCRIPTION;
import static com.example.projectlayout.ui.Alarm.AlarmBroadcastReceiver.IMAGE;
import static com.example.projectlayout.ui.Alarm.AlarmNotificationChannel.CHANNEL_ID;

public class AlarmService extends Service {

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;


    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
        mediaPlayer.setLooping(true);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, RingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        String alarmTitle = String.format("%s Alarm", intent.getStringExtra(DESCRIPTION));


        byte[] image = intent.getByteArrayExtra(IMAGE);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(alarmTitle)
                .setContentText("Ring Ring .. Ring Ring")
                //.setLargeIcon(bitmap)
                .setContentIntent(pendingIntent)
                .build();

        mediaPlayer.start();

        long[] pattern = { 0, 100, 1000 };


        //vibrator.vibrate(VibrationEffect.createPredefined(EFFECT_DOUBLE_CLICK));

        //DEFAULT_AMPLITUDE,USAGE_ALARM);
        vibrator.vibrate(pattern, 0);

        startForeground(1, notification);

        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();
        vibrator.cancel();
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
