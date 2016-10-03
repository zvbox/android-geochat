package com.zv.geochat.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zv.geochat.provider.GeoChatProviderMetadata.*;

/**
 * Helps open, create, and upgrade the database file
 */
class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private Context mContext;

    DatabaseHelper(Context context) {
        super(context,
                GeoChatProviderMetadata.DATABASE_NAME,
                null,
                GeoChatProviderMetadata.DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "inner oncreate called");
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "inner onupgrade called");
        Log.w(TAG, "Upgrading database from version "
                + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        // ChatMessage
        db.execSQL("DROP TABLE IF EXISTS " +
                ChatMessageTableMetaData.TABLE_NAME);
        createTables(db);
    }


    private void createTables(SQLiteDatabase db) {
        // ChatMessage
        db.execSQL("CREATE TABLE IF NOT EXISTS " + ChatMessageTableMetaData.TABLE_NAME + " ("
                + ChatMessageTableMetaData._ID + " INTEGER PRIMARY KEY,"
                + ChatMessageTableMetaData.USER_NAME + " TEXT,"
                + ChatMessageTableMetaData.MSG_BODY + " TEXT"
                + ");");
    }
}
