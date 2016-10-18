package com.example.abdullah.mytodolistapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentPortrait extends android.app.Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_portrait, container, false);
        /*
        Button btn = (Button)view.findViewById(R.id.addTaskId1);
        if (Global.totalTasks == 0) {
            btn.setVisibility(View.VISIBLE);
        } else {
            btn.setVisibility(View.INVISIBLE);
        }*/
        gotoAddActivity(view);
        //populateListView1(view);
        refreshList(view);
        return view;
    }

    private void gotoAddActivity(View view) {
        Button addButton = (Button) view.findViewById(R.id.addTaskId1);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, 10);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (10) : {
                if (resultCode == Activity.RESULT_OK) {
                    Global.totalTasks++;
                    String taskName = data.getStringExtra("TASK_NAME");
                    String taskDate = data.getStringExtra("TASK_DATE");
                    String taskTime = data.getStringExtra("TASK_TIME");
                    MainActivity.myTasks.add(new Atask(taskName, taskDate, taskTime));
                    View v = getView();
                    populateListView1(v);
                }
                break;
            }
            case (20) : {
                if (resultCode == Activity.RESULT_OK) {
                    Global.totalTasks--;
                    MainActivity.myTasks.remove(Global.taskToBeDeleted);
                    View v = getView();
                    populateListView1(v);
                }
                break;
            }
        }
    }

    private void refreshList(View view) {
        Button refreshButton = (Button) view.findViewById(R.id.refreshListId1);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateListView1(v);
            }
        });
    }

    private void populateListView1(View v) {

        //Every time before populating sort the task on start date
        if (MainActivity.myTasks.size() > 1) {
            sortTaskList(0, (MainActivity.myTasks.size() - 1));
        }
        ArrayAdapter<Atask> myAdapter = new FragmentPortrait.MyListAdapter();
        ListView myListView = (ListView) getActivity().findViewById(R.id.listViewId1);
        myListView.setAdapter(myAdapter);

    }

    private void sortTaskList(int left, int right) {

        Date pDate = null;
        Date lDate = null;
        Date rDate = null;

        int i = left;
        int j = right;
        int pivot = (i + j) / 2;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            pDate = sdf.parse((MainActivity.myTasks.get(pivot).getTaskDate()) + "T" + MainActivity.myTasks.get(pivot).getTaskTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            lDate = sdf.parse((MainActivity.myTasks.get(i).getTaskDate()) + "T" + MainActivity.myTasks.get(i).getTaskTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            rDate = sdf.parse((MainActivity.myTasks.get(j).getTaskDate()) + "T" + MainActivity.myTasks.get(j).getTaskTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        while (i<=j) {
            while (lDate.compareTo(pDate) < 0) {
                i++;
                try {
                    lDate = sdf.parse((MainActivity.myTasks.get(i).getTaskDate()) + "T" + MainActivity.myTasks.get(i).getTaskTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            while (rDate.compareTo(pDate) > 0) {
                j--;
                try {
                    rDate = sdf.parse((MainActivity.myTasks.get(j).getTaskDate()) + "T" + MainActivity.myTasks.get(j).getTaskTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            //Now swap the Tasks
            if (i <= j) {
                Atask tempTask = MainActivity.myTasks.get(i);
                MainActivity.myTasks.set(i, MainActivity.myTasks.get(j));
                MainActivity.myTasks.set(j, tempTask);
                i++;
                j--;
            }
        }

        if (left < j) {
            sortTaskList(left, j);
        }
        if (right > i) {
            sortTaskList(i, right);
        }

    }

    private class MyListAdapter extends ArrayAdapter<Atask> {

        public MyListAdapter() {
            super(getActivity(), R.layout.a_task, MainActivity.myTasks);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.a_task, parent, false);
            }

            //Get task position
            Atask currentTask = MainActivity.myTasks.get(position);

            TextView viewName = (TextView) itemView.findViewById(R.id.taskNameId0);
            viewName.setText(currentTask.getTaskName());
            TextView viewDate = (TextView) itemView.findViewById(R.id.taskDateId0);
            viewDate.setText(currentTask.getTaskDate());
            TextView viewTime = (TextView) itemView.findViewById(R.id.taskTimeId0);
            viewTime.setText(currentTask.getTaskTime());

            Button startButton = (Button) itemView.findViewById(R.id.startId);
            startButton.setTag(position);

            Button deleteButton = (Button) itemView.findViewById(R.id.deleteId);
            deleteButton.setTag(position);

            // Start button
            startButton.setOnClickListener(new View.OnClickListener() {

                private int taskId;

                @Override
                public void onClick(View view) {
                    int position=(Integer)view.getTag();
                    ListView myListView = (ListView) getActivity().findViewById(R.id.listViewId1);
                    View v = myListView.getAdapter().getView(position, null, myListView);
                    TextView textView1 = (TextView) v.findViewById(R.id.taskNameId0);
                    String name = textView1.getText().toString();
                    //TextView textView2 = (TextView) v.findViewById(R.id.taskIndexId0);
                    //int taskId = Integer.parseInt(textView2.getText().toString());
                    Intent intent;
                    intent = new Intent(getActivity(), StartActivity.class);
                    intent.putExtra("ACTIVITY_NAME", name);
                    startActivityForResult(intent, 20);
                    Global.taskToBeDeleted = position;
                }

            });

            //Delete button
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=(Integer)view.getTag();
                    MainActivity.myTasks.remove(position);
                    Global.totalTasks--;
                    populateListView1(view);
                }
            });

            return itemView;

        }

    }

}