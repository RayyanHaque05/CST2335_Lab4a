package com.cst2335.lab4;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<TodoItem> todoList = new ArrayList<>();
    private TodoAdapter adapter;

    private ListView listView;
    private EditText etTodo;
    private SwitchCompat switchUrgent;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        listView = findViewById(R.id.list_view_todo);
        etTodo = findViewById(R.id.et_todo);
        switchUrgent = findViewById(R.id.switch_urgent);
        btnAdd = findViewById(R.id.btn_add);

        adapter = new TodoAdapter();
        listView.setAdapter(adapter);

        // When Add is clicked: create TodoItem, clear EditText, refresh adapter
        btnAdd.setOnClickListener(v -> {
            String text = etTodo.getText().toString();
            if (!text.trim().isEmpty()) {
                boolean isUrgent = switchUrgent.isChecked();
                todoList.add(new TodoItem(text, isUrgent));
                etTodo.setText("");
                switchUrgent.setChecked(false);
                adapter.notifyDataSetChanged();
            }
        });

        // Set onItemLongClickListener to show delete confirmation AlertDialog
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.dialog_title);
            builder.setMessage(getString(R.string.dialog_message, position));

            builder.setPositiveButton(R.string.btn_yes, (dialog, which) -> {
                todoList.remove(position);
                adapter.notifyDataSetChanged();
            });

            builder.setNegativeButton(R.string.btn_no, (dialog, which) -> {
                dialog.dismiss();
            });

            builder.create().show();
            return true;
        });
    }

    /**
     * Custom BaseAdapter implementation for the ToDo ListView.
     */
    private class TodoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return todoList.size();
        }

        @Override
        public Object getItem(int position) {
            return todoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_todo, parent, false);
            }

            TodoItem item = (TodoItem) getItem(position);
            TextView tv = view.findViewById(R.id.tv_todo_text);
            tv.setText(item.getText());

            if (item.isUrgent()) {
                view.setBackgroundColor(Color.RED);
                tv.setTextColor(Color.WHITE);
            } else {
                view.setBackgroundColor(Color.WHITE);
                tv.setTextColor(Color.BLACK);
            }

            return view;
        }
    }
}
