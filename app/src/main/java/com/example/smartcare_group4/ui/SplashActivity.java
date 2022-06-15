package com.example.smartcare_group4.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcare_group4.R;
import com.example.smartcare_group4.ui.init.InitActivity;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        spinner = (ProgressBar)findViewById(R.id.progressBar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, InitActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);

    }
}