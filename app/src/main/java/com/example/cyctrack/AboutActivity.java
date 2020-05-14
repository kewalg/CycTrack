package com.example.cyctrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cyctrack.Model.Weather;

public class AboutActivity extends AppCompatActivity {

    Button btnhome_aboutus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        btnhome_aboutus = findViewById(R.id.btn_home_aboutus);


        btnhome_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AboutActivity.this, WeatherActivity.class);
                startActivity(i);
            }
        });
    }
}
