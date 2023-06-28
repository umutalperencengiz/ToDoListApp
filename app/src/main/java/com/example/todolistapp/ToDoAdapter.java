package com.example.todolistapp;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> todolist;
    private MainActivity  activity;
    private DataBaseHandler db;

    public ToDoAdapter(DataBaseHandler db,MainActivity activity){
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasklayout,parent,false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        db.openDatabase();
        final ToDoModel item = todolist.get(position);
        holder.task.setText(item.getText());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    db.updateStatus(item.getId(),1);
                }
                else db.updateStatus(item.getId(),0);
            }
        });
    }
    public Context getcontext(){return activity;}
    public void deleteItem(int position){
        ToDoModel item = todolist.get(position);
        db.deleteTask(item.getId());
        todolist.remove(position);
        notifyItemRemoved(position);
    }
    public void editTask(int position){
        ToDoModel item = todolist.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getText());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(),AddNewTask.Tag);

    }

    private boolean toBoolean(int n) {
        return n != 0;
    }
    @Override
    public int getItemCount() {
        return todolist.size();
    }
    public void setTask(List<ToDoModel> todolist){
        this.todolist = todolist;
        notifyDataSetChanged();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;

        public ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);



        }

    }


}
