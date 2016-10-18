package com.example.abdullah.mytodolistapp;

/**
 * Created by Abdullah on 9/25/2016.
 */
public class Atask {
    private String taskName;
    private String taskDate;
    private String taskTime;

    public Atask(String taskName, String taskDate, String taskTime) {
        this.taskName = taskName;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }
}
