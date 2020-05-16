package com.example.cyctrack;
// Importing required modules
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cyctrack.Model.Weather;

public class AboutActivity extends AppCompatActivity {
    // Declaring back button to go to home activity
    Button btnhome_aboutus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        // casting the button to build connection with xml file
        btnhome_aboutus = findViewById(R.id.btn_home_aboutus);

        //setting on click action on button which takes to home activity
        btnhome_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AboutActivity.this, WeatherActivity.class);
                startActivity(i);
            }
        });
    }
}
