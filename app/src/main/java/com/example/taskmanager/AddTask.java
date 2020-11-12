package com.example.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Todo;

import java.util.ArrayList;

public class AddTask extends Activity {
    ArrayList<Todo> todo = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        Button addTaskButton = AddTask.this.findViewById(R.id.addTaskButton2);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EditText titleNameInput = AddTask.this.findViewById(R.id.taskTitle);
//                EditText taskDescInput = AddTask.this.findViewById(R.id.taskDesc);
//                EditText taskProgress = AddTask.this.findViewById(R.id.taskProgress);

                String taskName = ((TextView) findViewById(R.id.taskTitle)).getText().toString();
                String taskDescription = ((TextView) findViewById(R.id.taskDesc)).getText().toString();
                String taskProgress = ((TextView) findViewById(R.id.taskProgress)).getText().toString();

                Todo awsSend = Todo.builder().name(taskName).description(taskDescription).progress(taskProgress).build();
                todo.add(awsSend);

                Amplify.API.mutate(
                        ModelMutation.create(awsSend),
                        success -> Log.i("Amplify", "Added Task"),
                        error -> Log.e("Amplify", error.toString())
                );

                Intent i = new Intent(AddTask.this, MainActivity.class);
                startActivity(i);
            }
        });
    };



    
    
}