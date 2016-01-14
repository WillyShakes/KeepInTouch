package com.willycode.keepintouch.Contacts.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Manuel ELO'O on 18/11/2015.
 */
public class ContactDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Contact.db";
    private static ContactDbHelper mDbHelper;

    /* Inner class that defines the table contents */
    public static abstract class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_LAST_CALL_TIME = "last_call_time";
        public static final String COLUMN_PERIOD = "period";
    }
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ContactEntry.TABLE_NAME + " (" +
                    ContactEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                    ContactEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    ContactEntry.COLUMN_NUMBER + TEXT_TYPE + COMMA_SEP +
                    ContactEntry.COLUMN_LAST_CALL_TIME + TEXT_TYPE + COMMA_SEP +
                    ContactEntry.COLUMN_PERIOD + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ContactEntry.TABLE_NAME;

    private ContactDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized ContactDbHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mDbHelper == null) {
            mDbHelper = new ContactDbHelper(context.getApplicationContext());
        }
        return mDbHelper;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}