package com.example.taskmanager;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskViewAdapter extends RecyclerView.Adapter<TaskViewAdapter.TaskViewHolder> {
    ArrayList<Task> tasks;
    public TaskViewAdapter(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);

        return new TaskViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.task = tasks.get(position);
        // Why is holder.fragment not working below
        ((TextView) holder.fragment.findViewById(R.id.taskName))
        .setText(holder.task.taskName + " : " + holder.task.taskDesc + " : " + holder.task.taskProgress);

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        Task task;
        View fragment;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            this.fragment = itemView;
        }
    }
}
