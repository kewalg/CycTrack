package com.example.cyctrack;

// Importing necessary modules
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.CheckedTextView;

public class SafetyActivity extends AppCompatActivity {

    // Setting splash time out to be 2 secs
    private static int SPLASH_TIME_OUT = 2000;


    // Declaring variables
    CheckedTextView checkedview1, checkedview2, checkedview3, checkedview4, checkedview5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_safety);

        // Casting variables with connection to xml file
        checkedview1 = findViewById(R.id.ctv1);

        // Setting the check box to be ticked
        checkedview1.setChecked(true);
        checkedview2 = findViewById(R.id.ctv2);
        checkedview2.setChecked(true);
        checkedview3 = findViewById(R.id.ctv3);
        checkedview3.setChecked(true);
        checkedview4 = findViewById(R.id.ctv4);
        checkedview4.setChecked(true);
        checkedview5 = findViewById(R.id.ctv5);
        checkedview5.setChecked(true);


        // when time runs out, putting an intent to go to Map activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SafetyActivity.this, MapTestActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
