package com.example.abdullah.mytodolistapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private static String taskName;
    private static String taskDate;
    private static String taskTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Intent intent = getIntent();

        addTask();
        listenCancel();
    }

    private void listenCancel() {
        Button loginButton = (Button) findViewById(R.id.cancelId);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void addTask() {

        Button dtButton = (Button) findViewById(R.id.saveId);
        dtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText usrTxt1 = (EditText) findViewById(R.id.taskNameId);
                AddActivity.taskName = usrTxt1.getText().toString();

                EditText usrTxt2 = (EditText) findViewById(R.id.taskDateId);
                AddActivity.taskDate = usrTxt2.getText().toString();

                EditText usrTxt3 = (EditText) findViewById(R.id.taskTimeId);
                AddActivity.taskTime = usrTxt3.getText().toString();

                TextView displayView = (TextView) findViewById(R.id.outText3);
                displayView.setText("Saved...!");


                String value = AddActivity.taskDate + "T" + AddActivity.taskTime;
                Date date = null;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    sdf.setLenient(false);
                    date = sdf.parse(value);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (date == null) {
                    displayView.setText("Wrong Date/Time Format...!");
                } else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("TASK_NAME", taskName);
                    resultIntent.putExtra("TASK_DATE", taskDate);
                    resultIntent.putExtra("TASK_TIME", taskTime);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        });
    }


}
