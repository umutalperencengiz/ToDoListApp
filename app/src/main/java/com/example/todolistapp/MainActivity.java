package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter todoadapter;
    private List<ToDoModel>tasklist;
    private DataBaseHandler db;
    private FloatingActionButton fab;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tasklist = new ArrayList<>();
        db = new DataBaseHandler(this);
        db.openDatabase();

        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        tasksRecyclerView = findViewById(R.id.taskitems);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        todoadapter = new ToDoAdapter(db,this);
        tasksRecyclerView.setAdapter(todoadapter);
        // retrieving datas from database
        tasklist =  db.getAllTasks();
        Collections.reverse(tasklist);
        todoadapter.setTask(tasklist);
        fab = findViewById(R.id.floatingActionButton);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(todoadapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.Tag);

            }
        });


    }
    @Override
    public void handleDialogClose(DialogInterface dialog){
        tasklist = db.getAllTasks();
        Collections.reverse(tasklist);
        todoadapter.setTask(tasklist);
        todoadapter.notifyDataSetChanged();
    }

}