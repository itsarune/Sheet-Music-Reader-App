package com.notarealcompany.arun.readmymusic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * This class contains methods that handle functions that manage the DigitalizedMusic Database. It
 * uses Android's built-in SQLite Open Database. There are functions that allow users to store a
 * new project or retrieve stored objects.
 *
 * <p>
 *     This class requires the following helper classes: DigitalMusicHelper and SampleDBContract.
 * </p>
 *
 * <p>
 *     The database contains the name of the project and the object data as a byte-array. When the
 * object is retrieved, the object is re-constructed from the byte-array. The DigitalizedMusic class
 * now contains functions that convert the Bitmap into a byte-array and reconstructs it into a
 * Bitmap from the database.
 * </p>
 *
 * @author Arun B.
 * @version 1.1, 22/5/20
 */
public class MusicDatabaseManager {
    private SQLiteDatabase database;
    private Context mContext;

    /**
     * Sole Constructor. It opens the database.
     *
     * @param mContext  Context, currently active application activity
     */
    public MusicDatabaseManager (Context mContext)
    {
        this.mContext = mContext;
        database = new DigitalMusicHelper(mContext).getWritableDatabase();
    }

    /**
     * Insert a new project into the database
     *
     * <p>
     *     For debugging purposes, the function pops a notification on the Android screen that
     * indicates the size of the database
     * </p>
     *
     * @param project   Project to be added into the database
     */
    public void insertNewProject(DigitalizedMusic project)
    {
        ContentValues values = new ContentValues();
        values.put(SampleDBContract.DigitalMusicProject.COLUMN_NAME, project.getNameOfMusic());

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        byte[] objectData;
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(byteOutput);
            oos.writeObject(project);
        } catch (IOException e) {
            e.printStackTrace();
        }
        objectData = byteOutput.toByteArray();
        values.put(SampleDBContract.DigitalMusicProject.COLUMN_OBJECT_DATA, objectData);

        long newRowId = database.insert(SampleDBContract.DigitalMusicProject.TABLE_NAME,
                null, values);
        Toast.makeText(mContext, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
    }

    /**
     * Returns an array of the project names that are currently in the database.
     *
     * @return  String, array of currently stored project titles
     */
    public String[] getExistingProjectNames()
    {
        Cursor cursor = database.query(
                SampleDBContract.DigitalMusicProject.TABLE_NAME,
                new String[] {SampleDBContract.DigitalMusicProject.COLUMN_NAME}, null, null,
                null, null, null);
        String[] projectNames = new String[cursor.getCount()];
        if (cursor!=null)
            cursor.moveToFirst();
        cursor.moveToFirst();
        int i = 0;
        while(cursor.isAfterLast() == false)
        {
            projectNames[i++] = cursor.getString(0);
            cursor.moveToNext();
        }
        cursor.close();
        return projectNames;
    }

    /**
     * Returns the size of the Database
     *
     * @return  int, integer size of the database
     */
    public int getDatabaseRowCount()
    {
        Cursor cursor = database.query(
                SampleDBContract.DigitalMusicProject.TABLE_NAME,
                new String[] {SampleDBContract.DigitalMusicProject.COLUMN_NAME}, null, null,
                null, null, null);
        return cursor.getCount();
    }

    /**
     * It imports a stored project into the current activity. The function find the object,
     * retrieves it and then proceeds to reconstruct the object from the byte-array. It returns the
     * reconstructed object.
     *
     * @param row_id    the row id of the database that contains the object
     * @return          DigitalizedMusic object, as it was stored into the database
     *                  null, if there was an error during the reconstruction process (user must
     *                      check for non-null output)
     */
    public DigitalizedMusic importProject(int row_id)
    {
        Cursor cursor = database.rawQuery("SELECT * FROM " +
                SampleDBContract.DigitalMusicProject.TABLE_NAME + " WHERE " +
                SampleDBContract.DigitalMusicProject._ID + " = " + (row_id), null);
        cursor.moveToFirst();

        if (cursor==null)
        {
            Log.d("ARUNS_DEBUG", "Error: Cursor is null!");
        }

        int columnId = cursor.getColumnIndex(SampleDBContract.DigitalMusicProject.COLUMN_OBJECT_DATA);
        byte[] objectData = cursor.getBlob(columnId);

        try
        {
            ByteArrayInputStream baip = new ByteArrayInputStream(objectData);
            ObjectInputStream ois = new ObjectInputStream(baip);
            DigitalizedMusic importedProject = (DigitalizedMusic) ois.readObject();
            Log.d("ARUNS_DEBUG", "OKAY");
            return importedProject;
        } catch (IOException e)
        {
          e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        Log.d("ARUNS_DEBUG", "UNSUCCESSFUL OPENING");
        return null;
    }

    /**
     * Returns ArrayList of DigitalizedMusic projects that are stored in the database. It calls
     * <code>importProject()</code> to reconstruct the objects and store it into the ArrayList.
     *
     * @return  ArrayList of DigitalizedMusic Objects, contains all the DigitalizedMusic objects
     *              contained within the database
     */
    public ArrayList<DigitalizedMusic> getAllProjects()
    {
        ArrayList<DigitalizedMusic> listOfProjects = new ArrayList<>();
        for (int i = 1; i <= getDatabaseRowCount(); ++i)
            listOfProjects.add(importProject(i));
        return listOfProjects;
    }

    /**
     * Closes the database for reading/writing.
     */
    public void closeDatabase()
    {
        database.close();
    }
}
