package com.example.projectlayout.ui.Alarm;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.projectlayout.AlarmService;
import com.example.projectlayout.R;

import static com.example.projectlayout.ui.Alarm.Alarm.DESCRIPTION;

public class testActivity extends Activity {

    public static final String CHANNEL_ID = "ALARM_SERVICE_CHANNEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //get the description from intent
        String alarmTitle = getIntent().getStringExtra(DESCRIPTION);

        //fetch alarm detail from database
        AlarmDatabase db = new AlarmDatabase(getApplicationContext());
        db.openReadable();
        Cursor cursor = db.getData("SELECT * FROM Alarm where description = \""+alarmTitle+"\";");
        cursor.moveToNext();

        //Convert image string to byte array then to bitmap
        String image = cursor.getString(13);
        byte[] recordImage = Base64.decode(image,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);

        //init the textview and set the description to the textview
        TextView one = findViewById(R.id.textView3);
        one.setText(alarmTitle);

        //init the imageview set the image to the image view
        ImageView imageView = findViewById(R.id.image_turn_off);
        imageView.setImageBitmap(bitmap);

        //init the button and set the button to turn off the alarm
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

