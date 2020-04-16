package com.example.cyctrack;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedBackActivity extends AppCompatActivity {
    UserReview myDb;

    Button btnSubmit;
    Button btnFeedback;
    EditText editName;
    EditText editFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        myDb = new UserReview(this);

        btnSubmit = findViewById(R.id.btnSubmit);
        btnFeedback = findViewById(R.id.btnFeedback);
        editName = findViewById(R.id.editName);
        editFeedback =findViewById(R.id.editFeedback);

        SubmitReview();
        viewFeedbacks();




    }
    public void SubmitReview(){
        btnSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(editName.getText().toString(),
                                editFeedback.getText().toString());
                        if(isInserted == true)
                            Toast.makeText(FeedBackActivity.this, "Review Submitted Successfully!! Thanks =)",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(FeedBackActivity.this,"Please submit your feedback :(",Toast.LENGTH_LONG).show();

                    }
                }
        );
    }

    public void viewFeedbacks(){
        btnFeedback.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor res = myDb.readFeedbacks();
                        if(res.getCount() == 0){
                            //show message
                            showMessage("Error","No Feedbacks Found");
                            return;
                        }
                        StringBuffer buffer = new StringBuffer();
                        while(res.moveToNext()){
                            buffer.append("Name: "+res.getString(1)+"\n");
                            buffer.append("Feedback: "+res.getString(2)+"\n\n");
                        }
                        // show all data
                        showMessage("Feedbacks Found !",buffer.toString());
                    }
                }
        );
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
}
