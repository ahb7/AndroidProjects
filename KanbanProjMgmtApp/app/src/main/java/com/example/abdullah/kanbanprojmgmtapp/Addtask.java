package com.example.abdullah.kanbanprojmgmtapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Addtask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        setTitle("Add a New Task");
        Intent intent = getIntent();
        finishAct();
    }

    public void saveTask(View view){

        EditText et1 = (EditText) findViewById(R.id.editText1);
        String task = et1.getText().toString();

        EditText et2 = (EditText) findViewById(R.id.editText2);

        String temp1 = et2.getText().toString();
        Log.d("in save temp", ":"+temp1);
        Integer prio = Integer.parseInt(temp1);
        Log.d("in save prio", ":"+prio);

        et1.setText("");
        et2.setText("");

        MainActivity.totalBacklogTasks++;
        MainActivity.backlogHash.put(prio, task);
        //You may have to push the other tasks
        //Log.i("DBG ", Integer.toString(MainActivity.totalNotes));
        Toast.makeText(Addtask.this, "The task is added!",
                Toast.LENGTH_SHORT).show();

    }

    private void finishAct() {
        Button finishButton = (Button) findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }


}
