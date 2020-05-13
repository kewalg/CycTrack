package com.example.cyctrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonSubmit, buttonHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_review);

        buttonHome = findViewById(R.id.home_btn);
        buttonSubmit = findViewById(R.id.btn_submit_item);
        buttonSubmit.setOnClickListener(this);
        buttonHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == buttonSubmit) {

            Intent intent = new Intent(getApplicationContext(), AddItem.class);
            startActivity(intent);
        }

        if (v == buttonHome) {

            Intent intent = new Intent(getApplicationContext(), WeatherActivity.class);
            startActivity(intent);
        }

    }
}
