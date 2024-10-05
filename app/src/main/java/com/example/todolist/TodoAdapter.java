package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TodoAdapter extends ArrayAdapter<Todo> {

    public TodoAdapter(Context context, List<Todo> todos) {
        super(context, 0, todos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Todo todo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_todo, parent, false);
        }

        TextView textViewDate = convertView.findViewById(R.id.textViewDate);
        TextView textViewComment = convertView.findViewById(R.id.textViewComment);

        textViewDate.setText(todo.getDate());
        textViewComment.setText(todo.getComment());

        return convertView;
    }
}