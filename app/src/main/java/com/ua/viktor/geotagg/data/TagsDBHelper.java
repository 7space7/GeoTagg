package com.ua.viktor.geotagg.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by viktor on 16.01.16.
 */
public class TagsDBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = TagsDBHelper.class.getSimpleName();



    private static final String DATABASE_NAME = "tags.db";
    private static final int DATABASE_VERSION = 1;

    public TagsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TAG_TABLE = "CREATE TABLE " +
                TagsContract.TagEntry.TABLE_TAGS + "(" + TagsContract.TagEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TagsContract.TagEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                TagsContract.TagEntry.COLUMN_COORD_LAT + " REAL NOT NULL, " +
                TagsContract.TagEntry.COLUMN_COORD_LONG + " REAL NOT NULL, " +
                TagsContract.TagEntry.COLUMN_DATE + " DATETIME NOT NULL, " +
                TagsContract.TagEntry.COLUMN_ICON + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_TAG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TagsContract.TagEntry.TABLE_TAGS);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                TagsContract.TagEntry.TABLE_TAGS + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }

}
