package com.example.cyctrack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class FeedBackActivity extends AppCompatActivity {

    Button btn_submit;
            Button btn_feedback;
            EditText edt_name;
            EditText edt_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        btn_submit = findViewById(R.id.btn_submit);
        

    }
}
