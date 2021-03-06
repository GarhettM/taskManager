package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        private void createNotificationChannel() {
//            // Create the NotificationChannel, but only on API 26+ because
//            // the NotificationChannel class is new and not in the support library
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                CharSequence name = getString(R.string.channel_name);
//                String description = getString(R.string.channel_description);
//                int importance = NotificationManager.IMPORTANCE_DEFAULT;
//                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//                channel.setDescription(description);
//                // Register the channel with the system; you can't change the importance
//                // or other notification behaviors after this
//                NotificationManager notificationManager = getSystemService(NotificationManager.class);
//                notificationManager.createNotificationChannel(channel);
//            }
//        }
        setTheTask();
        System.out.println(tasks.toString());
        RecyclerView rView = findViewById(R.id.recyclerView);
        rView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rView.setAdapter(new TaskViewAdapter(tasks));



//        Button taskOne = MainActivity.this.findViewById(R.id.taskOne);
//        taskOne.setOnClickListener(v -> {
//            Intent i = new Intent(MainActivity.this, TaskDetailPage.class);
//            String taskName = taskOne.getText().toString();
//            i.putExtra("task", taskName);
//            startActivity(i);
//        });
//
//        Button taskTwo = MainActivity.this.findViewById(R.id.taskTwo);
//        taskTwo.setOnClickListener(v -> {
//            Intent i = new Intent(MainActivity.this, TaskDetailPage.class);
//            String taskName = taskTwo.getText().toString();
//            i.putExtra("task", taskName);
//            startActivity(i);
//        });
//
//        Button taskThree = MainActivity.this.findViewById(R.id.taskThree);
//        taskThree.setOnClickListener(v -> {
//            Intent i = new Intent(MainActivity.this, TaskDetailPage.class);
//            String taskName = taskThree.getText().toString();
//            i.putExtra("task", taskName);
//            startActivity(i);
//        });
        
        Button addTaskButton = MainActivity.this.findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AddTask.class);
            startActivity(i);
        });

        Button allTaskButton = MainActivity.this.findViewById(R.id.alltasksButton);
        allTaskButton.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AllTasks.class);
            startActivity(i);
        });
    }

    public void setTheTask() {
        tasks.add(new Task("Do Dishes", "Do the dishes and clean counters", "inprogress"));
        tasks.add(new Task("Do laundry", "Clean al lclothes", "inprogress"));
        tasks.add(new Task("Do homework", "study hard for the money", "inprogress"));
        tasks.add(new Task("Do cat litter", "change cat litter", "inprogress"));
        tasks.add(new Task("Do chores", "Do everything else", "inprogress"));
    }
}