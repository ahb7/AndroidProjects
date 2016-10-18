package com.example.abdullah.mytodolistapp;

/**
 * Created by Abdullah on 9/25/2016.
 */
public class TaskHolder {
    private String data;
    public String getData() {return data;}
    public void setData(String data) {this.data = data;}

    private static final TaskHolder task = new TaskHolder();
    public static TaskHolder getInstance() {return task;}
}