package com.example.abdullah.kanbanprojmgmtapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    static HashMap<Integer, String> backlogHash = new HashMap<Integer, String>();
    static int totalBacklogTasks = 0;
    static HashMap<Integer, String> breakdownHash = new HashMap<Integer, String>();
    static int totalBreakdownTasks = 0;
    static HashMap<Integer, String> answerHash = new HashMap<Integer, String>();
    static int totalAnswerTasks = 0;
    static HashMap<Integer, String> validateHash = new HashMap<Integer, String>();
    static int totalValidateTasks = 0;

    static HashMap<Integer, ArrayList<String>> breakdownSubHash = new HashMap<Integer, ArrayList<String>>();
    //static HashMap totalBreakdownSubTasks = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("    Kanban Project Mgmt");
    }

    public void backlog(View view) {

        Intent intent = new Intent(MainActivity.this, Backlog.class);
        startActivityForResult(intent, 10);
        //startActivity(intent);

    }

    public void breakdown(View view) {

        Intent intent = new Intent(MainActivity.this, Breakdown.class);
        startActivity(intent);

    }

    public void answer(View view) {

        Intent intent = new Intent(MainActivity.this, Answer.class);
        startActivity(intent);

    }

    public void validate(View view) {

        Intent intent = new Intent(MainActivity.this, Validate.class);
        startActivity(intent);

    }


}


