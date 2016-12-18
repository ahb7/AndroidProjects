package com.example.abdullah.kanbanprojmgmtapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class EditBreakdown extends AppCompatActivity {

    static Integer newPhase = 0;
    static Integer theTask = Integer.MAX_VALUE;
    static boolean gridLayout = false;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter2;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public void onResume(){
        super.onResume();
        refreshTasks();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_breakdown);
        setTitle("    Edit Breakdown Task");
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b != null)
        {
            Integer pos = (Integer) b.get("TASK_POS");
            theTask = pos;
            Log.d("DBG Edit", ":"+pos);
        }

        //For drop down menu
        Spinner dropdown = (Spinner)findViewById(R.id.spinner2);
        String[] items = new String[]{"Move To Phase", "Answer", "Validate", "Backlog"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                switch (position) {
                    case 1:
                        newPhase = 1;
                        break;
                    case 2:
                        newPhase = 2;
                        break;
                    case 3:
                        newPhase = 3;
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        //Display the main task first
        int size = MainActivity.breakdownHash.size();
        Integer[] keys = new Integer[size];
        int i = 0;
        for(Integer key: MainActivity.breakdownHash.keySet()){
            keys[i] = key;
            Log.d("key",":"+key);
            i++;
        }
        //Sort the keys
        Arrays.sort(keys);
        int mainKey = keys[theTask];
        Log.d("MainKey", ":"+mainKey);
        String task = MainActivity.breakdownHash.get(mainKey);
        TextView tv1 = (TextView) findViewById(R.id.textView1);
        //TextView tv2 = (TextView) findViewById(R.id.textView2);
        tv1.setText("Main Task: "+task);

        if ( MainActivity.breakdownSubHash.get(mainKey) != null) {

            ArrayList<String> subTasks = new ArrayList<String>();
            subTasks = MainActivity.breakdownSubHash.get(mainKey);
            int size2 = subTasks.size();
            String[] tasks = new String[size2];

            i = 0;
            for (i = 0; i < size2; i++) {
                tasks[i] = subTasks.get(i);
                Log.d("Got task: ", ":"+tasks[i]);
                Log.d("Got size2: ", ":"+size2);
            }

            //setContentView(R.layout.activity_main);
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            adapter2 = new RecyclerAdapter3(tasks);
            if (gridLayout) {
                layoutManager = new GridLayoutManager(this, 2);
            } else {
                layoutManager = new LinearLayoutManager(this);
            }
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter2);
        }

        intent = getIntent();
        finishAct();
        refreshTasks();
        completeMove();

    }

    //This method deletes a tack and moves to a new phase
    private void completeMove() {
        Button moveButton = (Button) findViewById(R.id.moveButton5);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int size = MainActivity.breakdownHash.size();
                Integer[] keys = new Integer[size];
                int i = 0;
                for(Integer key: MainActivity.breakdownHash.keySet()){
                    keys[i] = key;
                    i++;
                }
                //Sort the keys
                Arrays.sort(keys);
                int toRemove = keys[theTask];
                String task = MainActivity.breakdownHash.get(toRemove);

                if (newPhase !=0) {
                    //delete the task
                    MainActivity.breakdownHash.remove(toRemove);
                    MainActivity.breakdownSubHash.remove(toRemove);
                    MainActivity.totalBreakdownTasks--;
                    //Add the task to new phase
                    if (newPhase == 3) {
                        MainActivity.backlogHash.put(toRemove, task);
                        MainActivity.totalBacklogTasks++;
                    } else if ( newPhase == 1) {
                        MainActivity.answerHash.put(toRemove, task);
                        MainActivity.totalAnswerTasks++;
                    } else if (newPhase == 2) {
                        MainActivity.validateHash.put(toRemove, task);
                        MainActivity.totalValidateTasks++;
                    }
                    Toast.makeText(EditBreakdown.this, "The task is moved!",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void addSubTask(View view) {

        Intent intent = new Intent(EditBreakdown.this, AddSubTask.class);
        //intent.putExtra("ACTIVITY_VAR", name);
        //startActivity(intent);
        startActivityForResult(intent, 200);

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

    public void refreshTasks(){

        //Display the main task first
        int size = MainActivity.breakdownHash.size();
        Integer[] keys = new Integer[size];
        int i = 0;
        for(Integer key: MainActivity.breakdownHash.keySet()){
            keys[i] = key;
            i++;
        }
        //Sort the keys
        Arrays.sort(keys);
        int mainKey = keys[theTask];

        if (MainActivity.breakdownSubHash.get(mainKey) == null) {
            return;
        }

        ArrayList<String> subTasks = MainActivity.breakdownSubHash.get(mainKey);
        int size2 = subTasks.size();
        String[] tasks = new String[size2];

        i = 0;
        for(i=0; i<size2; i++) {
            tasks[i] = subTasks.get(i);
        }

        //setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter2 = new RecyclerAdapter3(tasks);
        if (gridLayout) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter2);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (100) : {
                if (resultCode == Activity.RESULT_OK) {
                    //refreshTasks();
                    //MainActivity.myTasks.remove(taskId);
                }
                break;
            }
        }
    }
}
