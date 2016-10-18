package com.example.abdullah.mytodolistapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        TextView TextV1 = (TextView)findViewById(R.id.start_ActivityId);
        if(b != null)
        {
            String name =(String) b.get("ACTIVITY_NAME");
            TextV1.setText("Activity Started: " + name + "...");
        }

        startTimer();
        finishActivity();
    }

    public void startTimer() {

        Button btn1 = (Button) findViewById(R.id.cancel_acivity_id);
        btn1.setVisibility(View.VISIBLE);

        Button btn2 = (Button) findViewById(R.id.finish_activity_id);
        btn2.setVisibility(View.INVISIBLE);

        new CountDownTimer(181000, 1000) {
            int i = 180;
            @Override
            public void onTick(long millisUntilFinished) {
                TextView TextV2 = (TextView) findViewById(R.id.time_out_id);
                TextV2.setText("Time Left: " + i + " Sec...");
                i--;
                Button loginButton = (Button) findViewById(R.id.cancel_acivity_id);
                loginButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent resultIntent = new Intent();
                            setResult(Activity.RESULT_CANCELED, resultIntent);
                            finish();
                        }
                    });
            }

            @Override
            public void onFinish() {
                // do something end times 180s
                TextView TextV2 = (TextView) findViewById(R.id.time_out_id);
                TextV2.setText("Your Activity Done!");
                Button btn1 = (Button) findViewById(R.id.cancel_acivity_id);
                btn1.setVisibility(View.INVISIBLE);
                Button btn2 = (Button) findViewById(R.id.finish_activity_id);
                btn2.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void finishActivity() {
        Button finishButton = (Button) findViewById(R.id.finish_activity_id);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
