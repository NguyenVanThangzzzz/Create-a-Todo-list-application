package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class TodoAdapter extends ArrayAdapter<Todo> {

    private DatabaseHelper dbHelper;
    private OnTodoClickListener onTodoClickListener;

    public interface OnTodoClickListener {
        void onTodoClick(Todo todo);
        void onTodoCheckedChange(Todo todo, boolean isChecked);
    }

    public TodoAdapter(Context context, List<Todo> todos, OnTodoClickListener listener) {
        super(context, 0, todos);
        dbHelper = new DatabaseHelper(context);
        this.onTodoClickListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Todo todo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }

        CheckBox checkBoxTodo = convertView.findViewById(R.id.checkBoxTodo);
        TextView textViewDate = convertView.findViewById(R.id.textViewDate);
        TextView textViewComment = convertView.findViewById(R.id.textViewComment);

        checkBoxTodo.setChecked(todo.isChecked());
        textViewDate.setText(todo.getDate());
        textViewComment.setText(todo.getComment());

        checkBoxTodo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            todo.setChecked(isChecked);
            dbHelper.updateTodo(todo);
            if (onTodoClickListener != null) {
                onTodoClickListener.onTodoCheckedChange(todo, isChecked);
            }
        });

        convertView.setOnClickListener(v -> {
            if (onTodoClickListener != null) {
                onTodoClickListener.onTodoClick(todo);
            }
        });

        return convertView;
    }
}