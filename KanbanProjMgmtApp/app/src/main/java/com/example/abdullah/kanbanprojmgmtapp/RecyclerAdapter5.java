package com.example.abdullah.kanbanprojmgmtapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecyclerAdapter5 extends RecyclerView.Adapter<RecyclerAdapter5.RecycleViewHolder> {

    String[] taskNames, taskPrios;
    //private final View.OnClickListener mOnClickListener = new View.OnClickListener();

    public RecyclerAdapter5(String[] taskNames, String[] taskPrios) {
        this.taskNames = taskNames;
        this.taskPrios = taskPrios;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout1, parent, false);
        RecycleViewHolder recycleViewHolder = new RecycleViewHolder(view);
        return recycleViewHolder;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder holder, int position) {

        holder.tx_names.setText(taskNames[position]);
        holder.tx_grades.setText(taskPrios[position]);
    }

    @Override
    public int getItemCount() {
        return taskNames.length;
    }



    public static class RecycleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tx_names, tx_grades;
        private String mItem;
        private TextView mTextView;
        private Context context;

        public RecycleViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            tx_names = (TextView) itemView.findViewById(R.id.taskName);
            tx_grades = (TextView) itemView.findViewById(R.id.taskPrio);
            itemView.setOnClickListener(this);
        }

        public void setItem(String item) {
            mItem = item;
            mTextView.setText(item);
        }

        @Override
        public void onClick(View view) {

            context = itemView.getContext();
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Log.d("DBG0", ":"+getAdapterPosition());
                //Call the activity with this position
                Log.d("DBG", "I am in Adapter5");
                Intent intent = new Intent(context, EditValidate.class);
                intent.putExtra("TASK_POS", position);
                //context.startActivityForResult(intent, 1);
                context.startActivity(intent);
            }
        }
    }


}
