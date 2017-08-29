package com.austincreations.scorekeeper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Kevin on 8/29/2017.
 */

public class TransactionProvider extends ContentProvider {

    private static final String AUTHORITY = "com.austincreations.scorekeeper";
    private static final String BASE_PATH = "trans";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    //Constant to identify the requested operation
    private static final int TRANS = 2;
    private static final int TRANS_ID = 3;

    private static final UriMatcher uriMatcher =
            new UriMatcher(android.content.UriMatcher.NO_MATCH);

    public static final String CONTENT_ITEM_TYPE = "Transaction";

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, TRANS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TRANS_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (uriMatcher.match(uri) == TRANS_ID){
            selection = DBOpenHelper.TRANS_ID + "=" + uri.getLastPathSegment();
        }

        return database.query(DBOpenHelper.TABLE_TRANS, DBOpenHelper.TRANS_ALL_COLUMNS,
                selection, null, null, null, DBOpenHelper.TRANS_ID + " DESC");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = database.insert(DBOpenHelper.TABLE_TRANS,
                null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete(DBOpenHelper.TABLE_TRANS, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update(DBOpenHelper.TABLE_TRANS, values, selection, selectionArgs);
    }
}
