package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.db.TaskAdapter;
import com.example.todolist.db.TaskContract;
import com.example.todolist.db.TaskDbHelper;

//import java.util.ArrayList;

public class TodoListActivity extends AppCompatActivity {

    private static final String TAG = "TodoListActivity";
    private TaskDbHelper mHelper;
    public static final int REQUEST_CODE = 123;
    //private ListView mTaskListView;
    SQLiteDatabase db;
    //private ArrayAdapter<String> mAdapter;
    private TaskAdapter mAdapter;
    private CheckBox cbox;
    private TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new TaskDbHelper(this);
        db = mHelper.getReadableDatabase();
        setContentView(R.layout.activity_todo_list);
        RecyclerView recyclerView = findViewById(android.R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TaskAdapter(this,getAllItems());
        recyclerView.setAdapter(mAdapter);
//        View inflatedView = getLayoutInflater().inflate(R.layout.list_item, null);
//        cbox = inflatedView.findViewById(R.id.cbox);
//        cbox = findViewById(R.id.cbox);
//        texto = findViewById(R.id.texto);
//        cbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                ContentValues values = new ContentValues();
//                int check = 0;
//                if (cbox.isChecked()){
//                    check = 1;
//                }
//                values.put(TaskContract.TaskEntry.COL_TASK_CHECK, check);
//                db.update(TaskContract.TaskEntry.TABLE, values,TaskContract.TaskEntry.COL_TASK_TITLE + " = \""+ texto.getText()+"\"",null);
//            }
//        });
//        mTaskListView = (ListView)findViewById(android.R.id.list);
        //updateUI();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_todo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_logout){
            finish();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_new){
            Intent intent = new Intent(getApplicationContext(),AddTodo.class);
            startActivityForResult(intent,REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Todo todo = (Todo) data.getSerializableExtra(AddTodo.TODO);
                String task = String.valueOf(todo.content);
                int check = 0;
                if (todo.done){
                    check = 1;
                }
                //SQLiteDatabase db = mHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(TaskContract.TaskEntry.COL_TASK_TITLE,task);
                values.put(TaskContract.TaskEntry.COL_TASK_CHECK, check);
                db.insert(TaskContract.TaskEntry.TABLE,
                        null,
                        values);
                //db.close();
                mAdapter.swapCursor(getAllItems());
                //updateUI();
                Toast.makeText(getApplicationContext(),"Result ok content: " + todo.content,Toast.LENGTH_SHORT).show();
            }else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),"Result canceled !",Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private void updateUI() {
//        ArrayList<String> taskList = new ArrayList<>();
//        SQLiteDatabase db = mHelper.getReadableDatabase();
//        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
//                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},// TaskContract.TaskEntry.COL_TASK_CHECK},
//                null, null, null, null, null);
//        while (cursor.moveToNext()) {
//            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
//            taskList.add(cursor.getString(idx));
//        }
//
//        if (mAdapter == null) {
//            mAdapter = new ArrayAdapter<>(TodoListActivity.this,
//                    R.layout.list_item,
//                    R.id.texto,
//                    taskList);
//            mTaskListView.setAdapter(mAdapter);
//        } else {
//            mAdapter.clear();
//            mAdapter.addAll(taskList);
//            mAdapter.notifyDataSetChanged();
//        }
//
//        cursor.close();
//        db.close();
//    }

    private Cursor getAllItems(){
        return db.query(
                TaskContract.TaskEntry.TABLE,null,null,null,null,null,null);
    }
}