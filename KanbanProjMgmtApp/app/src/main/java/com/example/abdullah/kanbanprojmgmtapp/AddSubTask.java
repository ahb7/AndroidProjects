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
import java.util.Arrays;

public class AddSubTask extends AppCompatActivity {

    //static Integer theTask = Integer.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sub_task);
        setTitle("Add Sub Task");

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b != null)
        {
            Integer pos = (Integer) b.get("TASK_POS");
            //theTask = pos;
            Log.d("DBG Edit last", ":"+pos);
        }

        finishAct();
    }

    public void saveSubTask(View view) {

        EditText et1 = (EditText) findViewById(R.id.editText1);
        String subTask = et1.getText().toString();

        et1.setText("");

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

        if (MainActivity.breakdownSubHash.get(mainKey) == null) {
            ArrayList<String> AL = new ArrayList<String>();
            MainActivity.breakdownSubHash.put(mainKey, AL);
        }

        MainActivity.breakdownSubHash.get(mainKey).add(subTask);
        Toast.makeText(AddSubTask.this, "The Subtask is added!",
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
