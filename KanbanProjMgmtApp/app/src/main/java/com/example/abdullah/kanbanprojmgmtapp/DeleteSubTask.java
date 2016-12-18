package com.example.abdullah.kanbanprojmgmtapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import static android.R.id.list;

public class DeleteSubTask extends AppCompatActivity {

    Integer subTaskTobeDeleted = Integer.MAX_VALUE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_sub_task);
        setTitle("Delete Sub Task");

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            Integer pos = (Integer) b.get("TASK_POS");
            //theTask = pos;
            Log.d("DBG Delete Subtask ", ":" + pos);
            subTaskTobeDeleted = pos;
        }

        int size = MainActivity.breakdownHash.size();
        Integer[] keys = new Integer[size];
        int i = 0;
        for(Integer key: MainActivity.breakdownHash.keySet()){
            keys[i] = key;
            i++;
        }
        //Sort the keys
        Arrays.sort(keys);
        int mainKey = keys[EditBreakdown.theTask];

        if (MainActivity.breakdownSubHash.get(mainKey) != null) {
            TextView tv = (TextView) findViewById(R.id.textView2);
            tv.setText(MainActivity.breakdownSubHash.get(mainKey).get(subTaskTobeDeleted));
        }

        finishAct();
        deleteSubTask();

    }


    private void deleteSubTask() {
        Button finishButton = (Button) findViewById(R.id.button1);
        finishButton.setOnClickListener(new View.OnClickListener() {
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
                int mainKey = keys[EditBreakdown.theTask];

                Log.d("DBG", ":"+mainKey);
                if (MainActivity.breakdownSubHash.get(mainKey) != null) {
                    ArrayList<String> AL = MainActivity.breakdownSubHash.get(mainKey);
                    ArrayList<String> BL = new ArrayList<String>();
                    Log.d("DBG100", ":"+AL.size());
                    for (int k=0; k<AL.size(); k++) {
                        if(k != subTaskTobeDeleted){
                            BL.add(AL.get(k));
                        }
                    }
                    //AL.remove(subTaskTobeDeleted);
                    Log.d("DBG100", ":"+BL.size());
                    MainActivity.breakdownSubHash.put(mainKey, BL);
                    Toast.makeText(DeleteSubTask.this, "The Subtask is deleted!",
                            Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        });
    }

    private void finishAct() {
        Button finishButton = (Button) findViewById(R.id.button2);
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