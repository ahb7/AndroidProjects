package com.example.abdullah.kanbanprojmgmtapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.app.TaskStackBuilder;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.design.widget.FloatingActionButton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.Objects;

import static android.R.attr.key;

public class Backlog extends AppCompatActivity {

    static boolean gridLayout = false;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onResume(){
        super.onResume();
        refreshTasks();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backlog);
        setTitle("       Backlog Phase");

        int size = MainActivity.backlogHash.size();
        String[] tasks = new String[size];
        String[] prios = new String[size];
        Integer[] keys = new Integer[size];

        int i = 0;
        for(Integer key: MainActivity.backlogHash.keySet()){
            keys[i] = key;
            i++;
        }
        //Sort the keys
        Arrays.sort(keys);
        i = 0;
        for(Integer key: keys) {
            tasks[i] = Integer.toString(key);
            prios[i] = MainActivity.backlogHash.get(key);
            i++;
        }

        //setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new RecyclerAdapter1(tasks, prios);
        if (gridLayout) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        finishAct();
        refreshTasks();

    }

    public void addTask(View view) {

        Intent intent = new Intent(Backlog.this, Addtask.class);
        //startActivity(intent);
        startActivityForResult(intent, 100);

    }

    private void finishAct() {
        Button finishButton = (Button) findViewById(R.id.homeButton1);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    public void refreshTasks(){

        int size = MainActivity.backlogHash.size();
        String[] tasks = new String[size];
        String[] prios = new String[size];
        Integer[] keys = new Integer[size];

        int i = 0;
        for(Integer key: MainActivity.backlogHash.keySet()){
            keys[i] = key;
            i++;
        }
        //Sort the keys
        Arrays.sort(keys);
        i = 0;
        for(Integer key: keys) {
            tasks[i] = Integer.toString(key);
            prios[i] = MainActivity.backlogHash.get(key);
            i++;
        }

        //setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new RecyclerAdapter1(tasks, prios);
        if (gridLayout) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (100) : {
                if (resultCode == Activity.RESULT_OK) {
                    refreshTasks();
                    //MainActivity.myTasks.remove(taskId);
                }
                break;
            }
        }
    }

}
