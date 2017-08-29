package com.austincreations.scorekeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kevin on 8/29/2017.
 */

public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "score.db";
    public static final int DB_VERSION = 1;

    // for terms table
    public static final String TABLE_TRANS = "transactions";
    public static final String TRANS_ID = "_id";
    public static final String TRANS_TYPE = "type";
    public static final String TRANS_AMOUNT = "amount";
    public static final String[] TRANS_ALL_COLUMNS = {TRANS_ID, TRANS_TYPE, TRANS_AMOUNT};
    private static final String TRANS_TABLE_CREATE = "CREATE TABLE " + TABLE_TRANS + " (" +
            TRANS_ID + " INTEGER PRIMARY KEY, " + TRANS_TYPE + " TEXT, " + TRANS_AMOUNT + " INTEGER)";

    public DBOpenHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TRANS_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANS);
        onCreate(db);

    }
}
