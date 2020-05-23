package com.notarealcompany.arun.readmymusic;

import android.provider.BaseColumns;

/**
 * Helper class for MusicDatabaseManager. It contains the details to define and create the columns
 * and tables for the project database.
 *
 * @author Arun B.
 * @version 1.0, 20/4/20
 */
public final class SampleDBContract {
    /**
     * Sole constructor.
     */
    private SampleDBContract() { }

    /**
     * A sub-class that contains columns titles and details for the DigitalMusicProject database.
     *
     * <p>
     *     The CREATE_TABLE string contains the SQL code to create the structure of the database.
     * </p>
     *
     * @author Arun B.
     * @version 1.0, 20/4/20
     */
    public static class DigitalMusicProject implements BaseColumns
    {
        public static final String TABLE_NAME = "music_project";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_OBJECT_DATA = "notes";


        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " ( " +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_OBJECT_DATA + " BLOB" + ")";
    }
}
