package kcltech.kcltechtodo;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class TaskListAdapter extends BaseAdapter
{
    //Data
    private Activity activity;
    private ArrayList<Task> tasks = null;

    public TaskListAdapter(Activity activity, ArrayList<Task> tasks)
    {
        this.activity = activity;
        this.tasks = tasks;
    }

    public void setTasks(ArrayList<Task> tasks)
    {
        this.tasks = tasks;
    }



    //Adapter components
    @Override
    public int getCount() {
        return tasks == null ? 0 : tasks.size();
    }

    @Override
    public Object getItem(int i) {
        return tasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        Task t = (Task) getItem(i);
        return t == null ? -1 : t.getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if(v == null)
        {
            v = activity.getLayoutInflater().inflate(R.layout.task_list_item,viewGroup,false);
        }

        TextView title = v.findViewById(R.id.title_view);
        TextView dueDate = v.findViewById(R.id.due_date);
        Task task = (Task) getItem(i);

        title.setText(task.getTitle());
        dueDate.setText(task.getDueDate().toString("d MMM YYYY"));

        return v;
    }
}
