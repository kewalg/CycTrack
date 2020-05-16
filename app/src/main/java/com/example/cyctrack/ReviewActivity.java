package com.example.cyctrack;

// Importing necessary modules
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener {

    // Declaring variables
    Button buttonSubmit, buttonHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_review);

        // Casting variables for connection with xml file
        buttonHome = findViewById(R.id.home_btn);
        buttonSubmit = findViewById(R.id.btn_submit_item);

        // Setting on click listener on buttons to make them do some action
        buttonSubmit.setOnClickListener(this);
        buttonHome.setOnClickListener(this);
    }

    // Setting on click function
    @Override
    public void onClick(View v) {

        // When submit button is clicked go to Add item activity
        if (v == buttonSubmit) {

            Intent intent = new Intent(getApplicationContext(), AddItem.class);
            startActivity(intent);
        }

        if (v == buttonHome) {


            // when home button is clicked go to weather activity
            Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
            startActivity(intent);
        }

    }
}
