package com.notarealcompany.arun.readmymusic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This is a helper class for MusicDataManager
 *
 * <p>
 *     It creates the SQLite Database if it doesn't exist.
 * </p>
 *
 */
public class DigitalMusicHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "digital_music_database";

    /**
     * Sole constructor.
     *
     * @param context   Context, currently-active activity
     */
    public DigitalMusicHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Required override. Creates the database.
     *
     * @param sqLiteDatabase    sqLiteDatabase, handled by Android
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(SampleDBContract.DigitalMusicProject.CREATE_TABLE);
    }

    /**
     * Required override. Recreates the database table.
     *
     * @param sqLiteDatabase    sqLiteDatabase, handled by Android
     * @param i                 int, handled by Android
     * @param i1                int, handled by Android
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SampleDBContract.DigitalMusicProject.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
