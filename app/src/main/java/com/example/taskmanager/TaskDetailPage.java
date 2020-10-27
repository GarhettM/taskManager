package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TaskDetailPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail_page);

        Intent intent = getIntent();
        System.out.println(intent.getExtras().getString("task"));
        TextView titleNameView = TaskDetailPage.this.findViewById(R.id.taskTitle);
        titleNameView.setText(intent.getExtras().getString("task"));
    }
}