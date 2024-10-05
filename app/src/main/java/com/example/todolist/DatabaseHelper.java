package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3; // Tăng lên từ 2
    private static final String DATABASE_NAME = "TodoDB";
    private static final String TABLE_TODO = "todos";

    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "date";
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_IS_CHECKED = "is_checked";
    private static final String KEY_IS_COMPLETED = "is_completed";
    private static final String KEY_COMPLETION_TIME = "completion_time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATE + " TEXT,"
                + KEY_COMMENT + " TEXT," + KEY_IS_CHECKED + " INTEGER,"
                + KEY_IS_COMPLETED + " INTEGER," + KEY_COMPLETION_TIME + " INTEGER" + ")";
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            // Thêm các cột mới
            db.execSQL("ALTER TABLE " + TABLE_TODO + " ADD COLUMN " + KEY_IS_COMPLETED + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_TODO + " ADD COLUMN " + KEY_COMPLETION_TIME + " INTEGER DEFAULT 0");
        }
    }

    public void addTodo(String date, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, date);
        values.put(KEY_COMMENT, comment);
        values.put(KEY_IS_CHECKED, 0); // 0 for false
        values.put(KEY_IS_COMPLETED, 0); // 0 for false
        values.put(KEY_COMPLETION_TIME, 0);
        db.insert(TABLE_TODO, null, values);
        db.close();
    }

    public List<Todo> getAllTodos() {
        List<Todo> todoList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(KEY_ID);
            int dateIndex = cursor.getColumnIndex(KEY_DATE);
            int commentIndex = cursor.getColumnIndex(KEY_COMMENT);
            int isCheckedIndex = cursor.getColumnIndex(KEY_IS_CHECKED);
            int isCompletedIndex = cursor.getColumnIndex(KEY_IS_COMPLETED);
            int completionTimeIndex = cursor.getColumnIndex(KEY_COMPLETION_TIME);

            do {
                Todo todo = new Todo(
                    idIndex != -1 ? cursor.getInt(idIndex) : -1,
                    dateIndex != -1 ? cursor.getString(dateIndex) : "",
                    commentIndex != -1 ? cursor.getString(commentIndex) : "",
                    isCheckedIndex != -1 && cursor.getInt(isCheckedIndex) == 1,
                    isCompletedIndex != -1 && cursor.getInt(isCompletedIndex) == 1,
                    completionTimeIndex != -1 ? cursor.getLong(completionTimeIndex) : 0
                );
                todoList.add(todo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return todoList;
    }

    public void updateTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, todo.getDate());
        values.put(KEY_COMMENT, todo.getComment());
        values.put(KEY_IS_CHECKED, todo.isChecked() ? 1 : 0);
        values.put(KEY_IS_COMPLETED, todo.isCompleted() ? 1 : 0);
        values.put(KEY_COMPLETION_TIME, todo.getCompletionTime());
        db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[]{String.valueOf(todo.getId())});
        db.close();
    }

    public void deleteTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, KEY_ID + " = ?",
                new String[]{String.valueOf(todo.getId())});
        db.close();
    }

    public void markTodoAsCompleted(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IS_COMPLETED, 1);
        values.put(KEY_COMPLETION_TIME, System.currentTimeMillis());
        db.update(TABLE_TODO, values, KEY_ID + " = ?",
                new String[]{String.valueOf(todo.getId())});
        db.close();
    }

    public void deleteCompletedTodos() {
        SQLiteDatabase db = this.getWritableDatabase();
        long fiveMinutesAgo = System.currentTimeMillis() - (5 * 60 * 1000);
        db.delete(TABLE_TODO, KEY_IS_COMPLETED + " = 1 AND " + KEY_COMPLETION_TIME + " <= ?",
                new String[]{String.valueOf(fiveMinutesAgo)});
        db.close();
    }
}