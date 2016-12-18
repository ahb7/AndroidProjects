package com.example.abdullah.kanbanprojmgmtapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class EditBacklog extends AppCompatActivity {

    static Integer newPhase = 0;
    static Integer theTask = Integer.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_backlog);
        setTitle("    Edit Backlog Task");
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b != null)
        {
            Integer pos = (Integer) b.get("TASK_POS");
            theTask = pos;
            Log.d("DBG Edit", ":"+pos);
        }

        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        String[] items = new String[]{"Move To Phase", "Breakdown", "Answer", "Validate"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

                switch (position) {
                    case 1:
                        newPhase = 1;
                        Log.d("DBG", "Move to Breakdown Phase"+newPhase);
                        break;
                    case 2:
                        newPhase = 2;
                        Log.d("DBG", "Move to Answer Phase"+newPhase);
                        break;
                    case 3:
                        newPhase = 3;
                        Log.d("DBG", "Move to Validate Phase"+newPhase);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        //Display the task first
        int size = MainActivity.backlogHash.size();
        Integer[] keys = new Integer[size];
        int i = 0;
        for(Integer key: MainActivity.backlogHash.keySet()){
            keys[i] = key;
            i++;
        }
        //Sort the keys
        Arrays.sort(keys);
        int toRemove = keys[theTask];
        String task = MainActivity.backlogHash.get(toRemove);
        TextView tv1 = (TextView) findViewById(R.id.textView51);
        TextView tv2 = (TextView) findViewById(R.id.textView52);
        tv1.setText("Task: "+task);
        tv2.setText("Prio: "+(Integer.toString(toRemove)));

        //Call other onlcik functions
        finishAct();
        completeMove();
        editTask();
        deleteTask();
        editPriority();

    }

    //This method deletes a tack and moves to a new phase
    private void completeMove() {
        Button moveButton = (Button) findViewById(R.id.moveButton5);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int size = MainActivity.backlogHash.size();
                Integer[] keys = new Integer[size];
                int i = 0;
                for(Integer key: MainActivity.backlogHash.keySet()){
                    keys[i] = key;
                    i++;
                }
                //Sort the keys
                Arrays.sort(keys);
                int toRemove = keys[theTask];
                String task = MainActivity.backlogHash.get(toRemove);

                if (newPhase !=0) {
                    //delete the task
                    MainActivity.backlogHash.remove(toRemove);
                    MainActivity.totalBacklogTasks--;
                    //Add the task to new phase
                    if (newPhase == 1) {
                        MainActivity.breakdownHash.put(toRemove, task);
                        MainActivity.totalBreakdownTasks++;
                    } else if ( newPhase == 2) {
                        MainActivity.answerHash.put(toRemove, task);
                        MainActivity.totalAnswerTasks++;
                    } else if (newPhase == 3) {
                        MainActivity.validateHash.put(toRemove, task);
                        MainActivity.totalValidateTasks++;
                    }
                    Toast.makeText(EditBacklog.this, "The task is moved!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void editTask() {
        Button button1 = (Button) findViewById(R.id.editButton51);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et1 = (EditText) findViewById(R.id.editText51);
                String task = et1.getText().toString();
                et1.setText("");

                //Find the correct key in the hash
                int size = MainActivity.backlogHash.size();
                Integer[] keys = new Integer[size];
                int i = 0;
                for(Integer key: MainActivity.backlogHash.keySet()){
                    keys[i] = key;
                    i++;
                }
                //Sort the keys
                Arrays.sort(keys);
                int toEdit = keys[theTask];
                MainActivity.backlogHash.put(toEdit, task);
            }
        });
    }

    private void editPriority() {
        Button button1 = (Button) findViewById(R.id.editButton52);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et2 = (EditText) findViewById(R.id.editText52);
                String temp = et2.getText().toString();
                Integer prio = Integer.parseInt(temp);

                et2.setText("");

                //Find the correct key in the hash
                int size = MainActivity.backlogHash.size();
                Integer[] keys = new Integer[size];
                int i = 0;
                for(Integer key: MainActivity.backlogHash.keySet()){
                    keys[i] = key;
                    i++;
                }
                //Sort the keys
                Arrays.sort(keys);
                int toEdit = keys[theTask];
                String task = MainActivity.backlogHash.get(toEdit);
                MainActivity.backlogHash.remove(toEdit);
                MainActivity.backlogHash.put(prio, task);
            }
        });
    }

    private void deleteTask() {
        Button button1 = (Button) findViewById(R.id.deleteButton5);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Find the correct key in the hash
                int size = MainActivity.backlogHash.size();
                Integer[] keys = new Integer[size];
                int i = 0;
                for(Integer key: MainActivity.backlogHash.keySet()){
                    keys[i] = key;
                    i++;
                }
                //Sort the keys
                Arrays.sort(keys);
                int toEdit = keys[theTask];
                Log.d("DBG0", ":"+toEdit);
                MainActivity.backlogHash.remove(toEdit);
                Toast.makeText(EditBacklog.this, "The task is deleted from Kanban!",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void finishAct() {
        Button finishButton = (Button) findViewById(R.id.finishButton5);
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
