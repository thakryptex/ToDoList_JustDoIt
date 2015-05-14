package ru.kryptex.todo_sitepoint;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import ru.kryptex.todo_sitepoint.db.TaskContract;
import ru.kryptex.todo_sitepoint.db.TaskDBHelper;


public class MainActivity extends ListSupportActivity {

    private ListAdapter listAdapter;
    private TaskDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        updateUI();

//        SQLiteDatabase sqlDB = new TaskDBHelper(this).getWritableDatabase();
//        Cursor cursor = sqlDB.query(TaskContract.TABLE,
//                                    new String[]{TaskContract.Columns.TASK},
//                                    null, null, null, null, null);
//
//        cursor.moveToFirst();
//        while (cursor.moveToNext()) {
//            Log.d("MainActivity cursor",
//                    cursor.getString(
//                            cursor.getColumnIndexOrThrow(
//                                    TaskContract.Columns.TASK)));
//        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final EditText inputField = new EditText(this);
                builder.setTitle("Add a task")
                       .setMessage("What do you want to do?")
                       .setView(inputField)
                       .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               String task = inputField.getText().toString();
                               Log.d("MainActivity", task);

                               helper = new TaskDBHelper(MainActivity.this);
                               SQLiteDatabase db = helper.getWritableDatabase();
                               ContentValues values = new ContentValues();

                               values.clear();
                               values.put(TaskContract.Columns.TASK, task);

                               db.insertWithOnConflict(TaskContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
                               updateUI();
                           }
                       })
                       .setNegativeButton("Cancel", null);
                builder.create().show();
                return true;

            default:
                return false;
        }
    }

    //TODO проблемы с использованием material button, пока удалил: при попытке удалить из середины списка, выскакивает Error
    //возможно связано с использованием ListView, а не ScrollView судя по редми
    private void updateUI() {
        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK},
                null, null, null, null, null);

        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.task_view,
                cursor,
                new String[] {TaskContract.Columns.TASK},
                new int[] {R.id.taskTextView},
                0);
//        ListView listView = (ListView) findViewById(R.id.li);
//        listView.setAdapter(listAdapter);
        this.setListAdapter(listAdapter);
    }

    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();


        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                               TaskContract.TABLE,
                               TaskContract.Columns.TASK, task);

        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();

    }

}
