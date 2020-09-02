package com.example.projectlayout.intro;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.projectlayout.R;

public class introActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

}
