package com.example.todolist;

public class Todo {
    private int id;
    private String date;
    private String comment;
    private boolean isChecked;

    public Todo() {
    }

    public Todo(int id, String date, String comment, boolean isChecked) {
        this.id = id;
        this.date = date;
        this.comment = comment;
        this.isChecked = isChecked;
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
}