package com.example.cyctrack;

// Importing necessary modules

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.cyctrack.Model.Main;

public class SplashScreen extends AppCompatActivity {
    TextView tv_title_splash;

    // setting splash screen timeout to be 2 secs
    private static int SPLASH_TIME_OUT = 6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        tv_title_splash = findViewById(R.id.tv_title_splash);

        String title = tv_title_splash.getText().toString();
        SpannableString sstitle = new SpannableString(title);
        ForegroundColorSpan uicolor1 = new ForegroundColorSpan(getResources().getColor(R.color.ui_color));
        sstitle.setSpan(uicolor1, 7, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_title_splash.setText(sstitle);

        //// when time runs out, putting an intent to go to weather activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, WeatherActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
