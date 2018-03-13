package kcltech.kcltechtodo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class TaskListActivity extends AppCompatActivity
{
    //view components
    private ListView listView;

    //list view state
    private ArrayList<Task> tasks = new ArrayList<>();
    private TaskListAdapter listAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        setTitle(R.string.taskListActivityTitle);

        //find views
        listView = findViewById(R.id.taskListView);
        //set up adapter
        listAdapter = new TaskListAdapter(this,tasks);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                taskClicked((Task)listAdapter.getItem(i));
            }
        });

        //initial refresh
        refreshTasks();

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.createTask:
                Intent createTaskIntent = new Intent(TaskListActivity.this,EditTaskActivity.class);
                startActivity(createTaskIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void refreshTasks()
    {
        DbHelper dbHelper = new DbHelper(getApplicationContext());
        tasks = dbHelper.getIncompleteTasks();
        listAdapter.setTasks(tasks);
        listAdapter.notifyDataSetInvalidated();
    }


    private void taskClicked(final Task task)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        //Positive button - Set task to be completed
        dialogBuilder.setPositiveButton(
                R.string.taskListActivityCompleteButton,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        task.setComplete(true);
                        new DbHelper(getApplicationContext()).saveTask(task);
                        refreshTasks();
                    }
                }
        );

        //Neutral button - Change to Edit Task Activity
        dialogBuilder.setNeutralButton(
                R.string.taskListActivityEditButton,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent goToEditTask = new Intent(getApplicationContext(),EditTaskActivity.class);

                        //pass TaskId as an Extra to the new activity. Used to identify whether we are editing
                        //a task or creating a new one
                        goToEditTask.putExtra("task_id",task.getId());
                        startActivity(goToEditTask);
                    }
                }
        );

        //If the task has a message, display it in the dialog box
        if(task.getNotes() != null && task.getNotes().length() > 0)
        {
            dialogBuilder.setMessage(task.getNotes());
        }
        dialogBuilder.setCancelable(true);
        AlertDialog dialog = dialogBuilder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
