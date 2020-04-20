package com.example.cyctrack;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MicrophoneDirection;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toaaccess notification service
        NotificationManager n = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        //permissions granted?
        if (n.isNotificationPolicyAccessGranted()) {
            n.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
            /*Intent i = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(i);*/
        } else {
            // Ask the user to grant access
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }
}