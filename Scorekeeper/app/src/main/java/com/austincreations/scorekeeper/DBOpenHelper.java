package com.austincreations.scorekeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBOpenHelper extends SQLiteOpenHelper {

    // database variables
    private static final String DB_NAME = "score.db";
    private static final int DB_VERSION = 1;

    // for transaction table
    static final String TABLE_TRANS = "transactions";
    static final String TRANS_ID = "_id";
    static final String TRANS_TYPE = "type";
    static final String TRANS_AMOUNT = "amount";
    static final String[] TRANS_ALL_COLUMNS = {TRANS_ID, TRANS_TYPE, TRANS_AMOUNT};
    private static final String TRANS_TABLE_CREATE = "CREATE TABLE " + TABLE_TRANS + " (" +
            TRANS_ID + " INTEGER PRIMARY KEY, " + TRANS_TYPE + " TEXT, " + TRANS_AMOUNT + " INTEGER)";

    // constants to seperate income from payouts
    static final String TRANS_PAYOUT = "Payout";
    static final String TRANS_INCOME = "Income";

    DBOpenHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TRANS_TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        clearTable(db);

    }

    private void clearTable(SQLiteDatabase db){

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANS);
        onCreate(db);

    }
}
