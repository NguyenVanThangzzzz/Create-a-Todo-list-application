package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import android.graphics.Color;

import java.util.List;

public class TodoAdapter extends ArrayAdapter<Todo> {

    private DatabaseHelper dbHelper;
    private OnTodoClickListener onTodoClickListener;
    private boolean isEditable;

    public interface OnTodoClickListener {
        void onTodoClick(Todo todo);
        void onTodoCheckedChange(Todo todo, boolean isChecked);
        void onTodoCompleted(Todo todo);
    }

    public TodoAdapter(Context context, List<Todo> todos, OnTodoClickListener listener, boolean isEditable) {
        super(context, 0, todos);
        dbHelper = new DatabaseHelper(context);
        this.onTodoClickListener = listener;
        this.isEditable = isEditable;
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

        textViewDate.setText(todo.getDate());
        textViewComment.setText(todo.getComment());

        if (isEditable) {
            checkBoxTodo.setVisibility(View.VISIBLE);
            checkBoxTodo.setChecked(todo.isChecked());
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
        } else {
            checkBoxTodo.setVisibility(View.GONE);
            if (todo.isCompleted()) {
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_green));
            } else {
                convertView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
            }
            convertView.setOnClickListener(v -> {
                if (!todo.isCompleted()) {
                    todo.setCompleted(true);
                    todo.setCompletionTime(System.currentTimeMillis());
                    dbHelper.markTodoAsCompleted(todo);
                    notifyDataSetChanged();
                    if (onTodoClickListener != null) {
                        onTodoClickListener.onTodoCompleted(todo);
                    }
                }
            });
        }

        return convertView;
    }
}