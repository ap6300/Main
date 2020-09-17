package com.example.projectlayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.projectlayout.ui.Alarm.testActivity;

import static com.example.projectlayout.ui.Alarm.Alarm.DESCRIPTION;
import static com.example.projectlayout.ui.Alarm.testActivity.CHANNEL_ID;

public class AlarmService extends Service {
    public AlarmService() {
    }
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = MediaPlayer.create(this,R.raw.alarm);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);



    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent fullScreenIntent = new Intent(this, testActivity.class );
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        String alarmTitle = fullScreenIntent.getStringExtra(DESCRIPTION);

        Toast.makeText(getApplicationContext(), alarmTitle, Toast.LENGTH_SHORT).show();


        int notificationId = 0;//Integer.parseInt(intent.getStringExtra(ID));



        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setContentTitle(alarmTitle)
                .setContentText(String.valueOf(notificationId))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(fullScreenPendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());
        return START_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onDestroy() {
        super.onDestroy();

        mediaPlayer.stop();

    }



    private void createNotificationChannel() {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            String description = "Alarm";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Alarm Service Channel", importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
    }
}
