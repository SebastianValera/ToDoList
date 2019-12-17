package com.example.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.TodoListActivity;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private TaskDbHelper mHelper;

    public TaskAdapter(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item,parent,false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){
            return;
        }
        final String name = mCursor.getString(mCursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE));
        int marcado = mCursor.getInt(mCursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_CHECK));

        holder.task.setText(name);
        if (marcado == 1){
            holder.check.setChecked(true);
        }else{
            holder.check.setChecked(false);
        }
        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mHelper = new TaskDbHelper(mContext);
                SQLiteDatabase db = mHelper.getReadableDatabase();
                ContentValues values = new ContentValues();
                int check = 0;
                if (isChecked){
                    check = 1;
                }
                values.put(TaskContract.TaskEntry.COL_TASK_CHECK, check);
                db.update(TaskContract.TaskEntry.TABLE, values,TaskContract.TaskEntry.COL_TASK_TITLE + " = \""+ name +"\"",null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor){
        if (mCursor != null){
            mCursor.close();
        }
        mCursor = newCursor;

        if (newCursor != null){
            notifyDataSetChanged();
        }
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder{
        public TextView task;
        public CheckBox check;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.texto);
            check = itemView.findViewById(R.id.cbox);
        }
    }
}
