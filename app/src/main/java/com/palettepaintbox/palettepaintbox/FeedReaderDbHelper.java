package com.palettepaintbox.palettepaintbox;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

/*
 * This is the class that helps to read and write to the SQLite local database.
 */
public class FeedReaderDbHelper extends SQLiteOpenHelper {

    /*
     * Sets up initial tables on first run
     */
    private static final String SQL_CREATE_PALETTES =
            "CREATE TABLE IF NOT EXISTS Palettes(" +
                    "paletteID" + " INTEGER PRIMARY KEY," +
                    "paletteName" + " TEXT);";

    private static final String SQL_CREATE_PALETTESTOCOLORS =
            "CREATE TABLE IF NOT EXISTS PalettesToColors(" +
                    "pID" + " INTEGER," +
                    "pColor" + " TEXT);";

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    // Constructor
    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Executes SQL to create the database
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PALETTES);
        db.execSQL(SQL_CREATE_PALETTESTOCOLORS);
    }

    // on upgrade
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL("");
        onCreate(db);
    }

    // on downgrade
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
