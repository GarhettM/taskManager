package com.example.taskmanager;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.app.NotificationCompat;

public class AddTask extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        Button addTaskButton = AddTask.this.findViewById(R.id.addTaskButton2);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText titleNameInput = AddTask.this.findViewById(R.id.taskTitle);
                EditText taskDescInput = AddTask.this.findViewById(R.id.taskDesc);

//                NotificationCompat.Builder builder = new NotificationCompat.Builder(AddTask.this, CHANNEL_ID)
//                        .setSmallIcon(R.drawable.ic_launcher_background)
//                        .setContentTitle(titleNameInput)
//                        .setContentText(taskDescInput)
//                        .setPriority(NotificationCompat.PRIORITY_MAX);


            }
        });
    };
    
}