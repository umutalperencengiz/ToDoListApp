package com.example.todolistapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private  static final String NAME = "ToDoListDB";
    private static final String ToDoTable ="ToDo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + ToDoTable + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)";
    private SQLiteDatabase db;
    DataBaseHandler(Context context){
        super(context,NAME,null,VERSION);


    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TODO_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int old_version,int new_version){
        //drop old table
        db.execSQL("DROP TABLE IF EXISTS " + ToDoTable);
        //Create table again
        onCreate(db);
    }
    public void openDatabase(){
        db = this.getWritableDatabase();
    }
    public void insertTask(ToDoModel todo){
        ContentValues cv= new ContentValues();
        cv.put(TASK,todo.getText());
        cv.put(STATUS,0);
        db.insert(ToDoTable,null,cv);

    }
    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> tasks = new ArrayList<>();
        Cursor cursor = null;
        db.beginTransaction();
        try{
            cursor = db.query(ToDoTable, null, null, null, null, null, null, null);
            if(cursor!=null){
                if(cursor.moveToFirst()){
                    while(cursor.moveToNext()){
                        ToDoModel task = new ToDoModel();
                        task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                        task.setText(cursor.getString(cursor.getColumnIndexOrThrow(TASK)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(STATUS)));
                        tasks.add(task);
                    }
                }

            }
        }finally{
            db.endTransaction();
            assert cursor != null;
            cursor.close();
        }
        return tasks;
    }
    public void updateStatus(int id,int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS,status);
        db.update(ToDoTable,cv,ID + "= ?",new String[] {String.valueOf(id)});
    }
    public void updateTask(int id,String task){
        ContentValues cv = new ContentValues();
        cv.put(TASK,task);
        db.update(ToDoTable,cv,ID+ "= ?",new String[] {String.valueOf(id)});
    }
    public void deleteTask(int id){
        db.delete(ToDoTable,ID +"= ?",new String[] {String.valueOf(id)});
    }



}
