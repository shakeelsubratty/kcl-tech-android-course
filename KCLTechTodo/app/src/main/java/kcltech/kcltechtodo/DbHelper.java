package kcltech.kcltechtodo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by shakeelsubratty on 20/02/2018.
 */

/*
Custom Database Helper to store Task object
 */
public class DbHelper extends SQLiteOpenHelper
{
    //Name and current Version of database

    public static final String DB_NAME = "DB";
    public static final int DB_VERSION = 1;

    /*
    DBHelper Constructor
     */
    public DbHelper(Context context)
    {
        super(context,DB_NAME,null,DB_VERSION);
    }

    /*
    Called when database is first created.
    We will consider this as upgrading from version O to version 1.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        onUpgrade(sqLiteDatabase, 0, DB_VERSION);
    }

    /*
    Called when updating the database from an oldVersion to a newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        switch(oldVersion)
        {
            case 0:
                //create intiial database using SQL
                sqLiteDatabase.execSQL("CREATE TABLE Tasks (" +
                        "id INTEGER PRIMARY KEY," +
                        "title TEXT," +
                        "notes TEXT," +
                        "dueDate INTEGER," +
                        "isComplete INTEGER" +
                        ");");
            case 1:

            case 2:
                break;
            default:
                throw new IllegalArgumentException();

        }
    }

    /*
    Save a task to the database
     */
    public void saveTask(Task t)
    {
        SQLiteDatabase db = getWritableDatabase();
        if(db == null) return;

        //Insert Task t into the table "Tasks" using it's ContentValues.
        db.insertWithOnConflict(
                "Tasks",
                null,
                t.getContentValues(),
                SQLiteDatabase.CONFLICT_REPLACE     //If a Task exists with the same ID as is being inserted, replace it.
        );
    }

    /*
    Query method to retrieve the list of incomplete Tasks in the database an an ArrayList
     */

    public ArrayList<Task> getIncompleteTasks()
    {
        ArrayList<Task> output = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        if(db == null) return output;

        //Store result of SQL query in Cursor object
        Cursor rawTasks = db.rawQuery("SELECT * FROM Tasks WHERE isComplete = 0 ORDER BY dueDate ASC;",null);

        //Iterate over all tasks in Cursor, adding them to ArrayList<Task> output
        if(rawTasks.moveToFirst())
        {
            do {
                output.add(new Task(rawTasks)); //Construct a new Task using the cursor, and add it to the ArrayList
            } while (rawTasks.moveToNext());
        }

        rawTasks.close();   //Close cursor (finished with it)

        return output;
    }

    /**
     * Get a single task by it's task id. Used for loading a task to the edit task activity.
     */
    public Task getTask(long taskId)
    {
        SQLiteDatabase db = getReadableDatabase();
        if(db == null) return null;

        //Make an SQL query
        Cursor result = db.query(
                "Tasks",                    // To the table "Tasks"
                null,                    // All columns
                "id = ?",               // Where id = ? (a parameter)
                new String[]{taskId + ""},      // set the parameter to taskId
                null,
                null,
                null
        );

        //Construct new Task object from result of Cursor and return it.
        if(result.moveToFirst())
        {
            return new Task(result);
        }else{
            return null;
        }
    }
}
