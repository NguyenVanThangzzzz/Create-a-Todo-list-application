package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TODO = "todo";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_COMMENT = "comment";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_COMMENT + " TEXT" + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(db);
    }

    // Create
    public long addTodo(String date, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_COMMENT, comment);
        long id = db.insert(TABLE_TODO, null, values);
        db.close();
        return id;
    }

    // Read
    public List<Todo> getAllTodos() {
        List<Todo> todoList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Todo todo = new Todo();
                todo.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                todo.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
                todo.setComment(cursor.getString(cursor.getColumnIndex(COLUMN_COMMENT)));
                todoList.add(todo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return todoList;
    }

    // Update
    public int updateTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, todo.getDate());
        values.put(COLUMN_COMMENT, todo.getComment());
        return db.update(TABLE_TODO, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(todo.getId())});
    }

    // Delete
    public void deleteTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, COLUMN_ID + " = ?",
                new String[]{String.valueOf(todo.getId())});
        db.close();
    }
}