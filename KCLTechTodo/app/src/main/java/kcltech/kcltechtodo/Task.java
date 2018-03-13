package kcltech.kcltechtodo;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import org.joda.time.DateTime;

/*
Task Model object
 */

public class Task
{
    private long id = -1;    //If the id == -1, the Task object has never entered the database yet
    private String title;
    private String notes;
    private boolean isComplete;
    private DateTime dueDate;


    /*
    Constructor - creating a Task object from scratch
     */
    public Task(String title, String notes, boolean isComplete, DateTime dueDate)
    {
        this.title = title;
        this.notes = notes;
        this.isComplete = isComplete;
        this.dueDate = dueDate;
    }

    /*
    Constructor - creating a Task object from an entry in the database
     */
    public Task(Cursor input)
    {
        id = input.getLong(input.getColumnIndex("id"));
        title = input.getString(input.getColumnIndex("title"));
        notes = input.getString(input.getColumnIndex("notes"));
        isComplete = input.getInt(input.getColumnIndex("isComplete")) == 1;
        dueDate = new DateTime(input.getLong(input.getColumnIndex("dueDate")));
    }

    /*
    Represent our Task data as a ContentValues object - for easy insertion into the database
     */
    public ContentValues getContentValues()
    {
        ContentValues output = new ContentValues();

        //If the Task has been in the database before, use it's id.
        //Otherwise, the database will assign a new ID to it when it is added.
        if(id > 0) output.put("id",id);

        //Add the rest of the Task data to the content values object
        output.put("title",title);
        output.put("notes",notes);
        output.put("dueDate", dueDate.getMillis());
        output.put("isComplete", isComplete ? 1 : 0);

        return output;
    }


    public long getId() {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getNotes() {
        return notes;
    }

    public boolean isCompleted() {
        return isComplete;
    }

    public DateTime getDueDate() {
        return dueDate;
    }

    public void setComplete(boolean complete)
    {
        this.isComplete = complete;
    }

    @Override
    public String toString() {
        return getTitle() + " " +  getNotes() + " " + isCompleted() + " " + getDueDate().toString("d MM");
    }
}
