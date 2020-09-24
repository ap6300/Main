package com.example.projectlayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Base64;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.projectlayout.ui.Alarm.AlarmDatabase;
import com.example.projectlayout.ui.Alarm.testActivity;

import static com.example.projectlayout.ui.Alarm.Alarm.DESCRIPTION;
import static com.example.projectlayout.ui.Alarm.Alarm.ID;
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

        //pass the alarm title to test Activity
        String alarmTitle = intent.getStringExtra(DESCRIPTION);
        fullScreenIntent.putExtra(DESCRIPTION, alarmTitle);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        int notificationId = intent.getIntExtra(ID,0);

        AlarmDatabase db = new AlarmDatabase(getApplicationContext());
        db.openReadable();

        Cursor cursor = db.getData("SELECT * FROM Alarm where description = \""+alarmTitle+"\";");

        cursor.moveToNext();
        String image = cursor.getString(13);

        byte[] recordImage = Base64.decode(image,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);

        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentTitle(alarmTitle)
                .setContentText("Tap to turn off the alarm")
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                    .bigLargeIcon(null))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(fullScreenPendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());
        return START_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
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
