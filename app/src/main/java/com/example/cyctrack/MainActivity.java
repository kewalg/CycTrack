package com.example.cyctrack;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Image;
import android.media.MicrophoneDirection;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.example.cyctrack.Model.Main;
import com.example.cyctrack.Model.Weather;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView img_maps, img_weather, img_reviews, img_speedo;

        img_maps = findViewById(R.id.img_maps);
        img_weather = findViewById(R.id.img_weather);
        img_reviews = findViewById(R.id.img_reviews);
        img_speedo = findViewById(R.id.img_speedo);


        img_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(i);
            }
        });

        img_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, WeatherActivity.class);
                startActivity(i);
            }
        });

        img_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ReviewActivity.class);
                startActivity(i);
            }
        });

        img_speedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SpeedOMeter.class);
                startActivity(i);
            }
        });

     /*   //toaaccess notification service
        NotificationManager n = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        //permissions granted?
        if (n.isNotificationPolicyAccessGranted()) {
            n.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
            *//*Intent i = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(i);*//*
        } else {
            // Ask the user to grant access
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }*/
    }
}