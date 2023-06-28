package com.example.todolistapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {
    public static String Tag = "ActionBottomDialog";
    private EditText newTaskText;
    private Button newTaskButton;
    private DataBaseHandler db;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.DialogStyle);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        View view = layoutInflater.inflate(R.layout.addnewtask, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;

    }
    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        newTaskText = getView().findViewById(R.id.AddNewTask);
        newTaskButton = getView().findViewById(R.id.AddNewTaskButton);
        db  = new DataBaseHandler(getActivity());
        db.openDatabase();
        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle !=null){
            isUpdate = true;
            String task = bundle.getString("task");
            assert task != null;
            newTaskText.setText(task);
            if(task.length() > 0){
                newTaskButton.setTextColor(ContextCompat.getColor(getContext(),R.color.black));
            }
        }
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTaskButton.setEnabled(false);
                    newTaskButton.setTextColor(Color.GRAY);

                }
                else{
                    newTaskButton.setEnabled(true);
                    newTaskButton.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final boolean finalIsUpdate = isUpdate;
        newTaskButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();
                if(finalIsUpdate){db.updateTask(bundle.getInt("id"),text);}
                else{

                    ToDoModel task = new ToDoModel();
                    task.setText(text);
                    task.setStatus(0);
                    db.insertTask(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }

    }
}


