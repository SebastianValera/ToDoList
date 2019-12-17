package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class AddTodo extends AppCompatActivity {

    private EditText contentEditText;
    private CheckBox doneCheckBox;
    private Button saveButton;

    public static final String TODO = "todo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        contentEditText = findViewById(R.id.content_et);
        doneCheckBox = findViewById(R.id.done_cb);
        saveButton = findViewById(R.id.save_btn);

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String content = contentEditText.getText().toString();
                boolean isDone = doneCheckBox.isChecked();

                Todo todo = new Todo();
                todo.content = content;
                todo.done = isDone;
                Intent intent = new Intent();
                intent.putExtra(TODO,todo);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
