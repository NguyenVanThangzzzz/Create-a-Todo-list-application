package com.example.todolist;

public class Todo {
    private int id;
    private String date;
    private String comment;
    private boolean isChecked;
    private boolean isCompleted;
    private long completionTime;

    public Todo() {
    }

    public Todo(int id, String date, String comment, boolean isChecked, boolean isCompleted, long completionTime) {
        this.id = id;
        this.date = date;
        this.comment = comment;
        this.isChecked = isChecked;
        this.isCompleted = isCompleted;
        this.completionTime = completionTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public long getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(long completionTime) {
        this.completionTime = completionTime;
    }
}