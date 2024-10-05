package com.example.todolist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TodoAdapter.OnTodoClickListener {

    private DatabaseHelper dbHelper;
    private ListView listViewTodos;
    private TodoAdapter adapter;
    private List<Todo> todoList;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    private TextView textViewToday;
    private TextView textViewTomorrow;
    private ListView listViewTodosTomorrow;
    private TodoAdapter adapterTomorrow;
    private List<Todo> todoListTomorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        listViewTodos = findViewById(R.id.listViewTodos);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        textViewToday = findViewById(R.id.textViewToday);
        textViewTomorrow = findViewById(R.id.textViewTomorrow);
        listViewTodosTomorrow = findViewById(R.id.listViewTodosTomorrow);

        todoList = new ArrayList<>();
        todoListTomorrow = new ArrayList<>();
        adapter = new TodoAdapter(this, todoList, this);
        adapterTomorrow = new TodoAdapter(this, todoListTomorrow, this);
        listViewTodos.setAdapter(adapter);
        listViewTodosTomorrow.setAdapter(adapterTomorrow);

        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("EEEE dd/MM/yyyy", Locale.getDefault());

        loadTodos();

        fabAdd.setOnClickListener(v -> showAddTodoDialog());

        // Xóa hai dòng setOnItemClickListener cũ
    }

    private void loadTodos() {
        todoList.clear();
        todoListTomorrow.clear();
        List<Todo> allTodos = dbHelper.getAllTodos();
        for (Todo todo : allTodos) {
            if (todo.isChecked()) {
                todoListTomorrow.add(todo);
            } else {
                todoList.add(todo);
            }
        }
        adapter.notifyDataSetChanged();
        adapterTomorrow.notifyDataSetChanged();
    }

    private void showAddTodoDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_todo, null);
        dialogBuilder.setView(dialogView);

        TextView textViewDate = dialogView.findViewById(R.id.textViewDate);
        EditText editTextComment = dialogView.findViewById(R.id.editTextComment);

        textViewDate.setOnClickListener(v -> showDatePicker(textViewDate));

        dialogBuilder.setTitle("Add New Todo");
        dialogBuilder.setPositiveButton("Add", null); // Đặt null ở đây để tránh dialog tự động đóng
        dialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();

        // Ghi đè sự kiện click cho nút "Add"
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String date = textViewDate.getText().toString();
            String comment = editTextComment.getText().toString();

            if (date.isEmpty() || comment.isEmpty()) {
                // Hiển thị thông báo lỗi nếu có trường nào đó còn trống
                if (date.isEmpty()) {
                    textViewDate.setError("Please select a date");
                }
                if (comment.isEmpty()) {
                    editTextComment.setError("Please enter a comment");
                }
            } else {
                // Nếu cả hai trường đều có dữ liệu, thêm Todo mới và đóng dialog
                dbHelper.addTodo(date, comment);
                loadTodos();
                dialog.dismiss();
            }
        });
    }

    private void showUpdateDeleteDialog(final Todo todo) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update_delete, null);
        dialogBuilder.setView(dialogView);

        TextView textViewDate = dialogView.findViewById(R.id.textViewDate);
        EditText editTextComment = dialogView.findViewById(R.id.editTextComment);

        textViewDate.setText(todo.getDate());
        editTextComment.setText(todo.getComment());

        textViewDate.setOnClickListener(v -> showDatePicker(textViewDate));

        dialogBuilder.setTitle("Update/Delete Todo");
        dialogBuilder.setPositiveButton("Update", (dialog, whichButton) -> {
            todo.setDate(textViewDate.getText().toString());
            todo.setComment(editTextComment.getText().toString());
            dbHelper.updateTodo(todo);
            loadTodos();
        });
        dialogBuilder.setNeutralButton("Delete", (dialog, whichButton) -> {
            // Hiển thị dialog xác nhận xóa
            new AlertDialog.Builder(MainActivity.this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this todo?")
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    dbHelper.deleteTodo(todo);
                    loadTodos();
                })
                .setNegativeButton("No", null)
                .show();
        });
        dialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void showDatePicker(final TextView textView) {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            textView.setText(dateFormatter.format(calendar.getTime()));
        };

        new DatePickerDialog(MainActivity.this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onTodoClick(Todo todo) {
        showUpdateDeleteDialog(todo);
    }

    @Override
    public void onTodoCheckedChange(Todo todo, boolean isChecked) {
        loadTodos(); // Tải lại danh sách để cập nhật vị trí của todo
    }
}