package com.example.taskmanager;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.api.graphql.model.ModelSubscription;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Todo;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Todo> tasks = new ArrayList<>();
    Handler handleTodos;
    Handler handleTodoSubscription;
    RecyclerView recyclerView;
    Handler checkLogin;

    FusedLocationProviderClient locationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleTodos = new Handler(Looper.getMainLooper(), message -> {
            setupRecView();
            return false;
        });

        handleTodoSubscription = new Handler(Looper.getMainLooper(), message -> {
            recyclerView.getAdapter().notifyItemInserted(tasks.size());
            recyclerView.smoothScrollToPosition(tasks.size());
            return false;
        });


        checkLogin = new Handler(Looper.getMainLooper(), message -> {
            if (message.arg1 == 0) {
                Log.i("Amplify.login", "Handler: they were not logged in.");
            } else if (message.arg1 == 1) {
                Log.i("Amplify.login", "Hander: They are logged in.");
            } else {
                Log.i("Amplify.login", "true or false?");
            }
            return false;
        });

        askPermissions();
        try {
            configureAws();
        } catch (AmplifyException e) {
            e.printStackTrace();
        }

        getIsSignedIn();

        getDataAws();
        setUpTodoSubscription();
        buttonListener();
        configureLocation();
        askForLocation();


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

    public void askPermissions() {
//        ActivityCompat.requestPermissions(this, new String[Android]);
        registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }).launch();
    }

    public void askForLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> Log.i(Tag + "Location Found", location.toString()))
                .addOnFailureListener(error -> Log.e(Tag + "Location Not Found", error.toString()))
                .addCanceledListener(() -> Log.e(Tag + "Location Canceled", "Location Canceled"))
                .addOnCompleteListener(complete -> Log.e(Tag + "Location Completed", complete.toString()));
    }

    public void configureLocation() {
        locationProvider = LocationServices.getFusedLocationProviderClient(this);
    }

    public void setUpTodoSubscription() {
        Amplify.API.subscribe(
                ModelSubscription.onCreate(Todo.class),
                onEstablish -> Log.i("Amplify", "Establish"),
                created -> {
                    Log.i("Amplify", "Enrolled");
                    Todo todo = created.getData();
                    handleTodoSubscription.sendEmptyMessage(1);
                },
                failure -> Log.e("Amplify", failure.toString()),
                () -> Log.i("Amplify", "complete")
        );
    }
    public void getDataAws() {
        Amplify.API.query(
                ModelQuery.list(Todo.class),
                success -> {
                    Log.i("Amplify", "Tasks Loaded");
                    for(Todo todo : success.getData()) {
                        tasks.add(todo);
                    }
                    handleTodos.sendEmptyMessage(1);
                },
                error -> Log.i("Amplify", error.toString())
                );
    }

    public void configureAws() throws AmplifyException {
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
        } catch (AmplifyException e) {
            e.printStackTrace();
        }
    }

    public void setupRecView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new TaskViewAdapter(tasks));
    }

    public void buttonListener() {
        Button signIn = findViewById(R.id.signIn);
        Button signUp = findViewById(R.id.signUp);

        signIn.setOnClickListener(v -> this.startActivity(new Intent (this, SignIn.class)));
        signUp.setOnClickListener(v -> this.startActivity(new Intent (this, SignUp.class)));
    }

    public void getIsSignedIn() {
//        boolean[] isSignedIn = {false};
        Amplify.Auth.fetchAuthSession(
                result -> {
                    Log.i("Amplify.login", result.toString());
                    Message message = new Message();
                    if(result.isSignedIn()) {

                        message.arg1 = 1;
                        checkLogin.sendMessage(message);
                    } else {
                        message.arg1 = 0;
                        checkLogin.sendMessage(message);
                    }
                },
                error -> Log.e("Amplify.login", error.toString())
        );
        Log.i("Amplify.login", "checking sync");
    }

    public void verifyUser() {
        Amplify.Auth.signUp(
                "username",
                "Password123",
                AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), "my@email.com").build(),
                result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
                error -> Log.e("AuthQuickStart", "Sign up failed", error)
        );
    }
}
