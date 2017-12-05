package com.pollub.ikms.ikms_mobile;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.pollub.ikms.ikms_mobile.data.DBHelper;
import com.pollub.ikms.ikms_mobile.data.NotificationsContract;
import com.pollub.ikms.ikms_mobile.data.SendersContract;

import static com.pollub.ikms.ikms_mobile.data.NotificationsContract.CONTENT_AUTHORITY;
import static com.pollub.ikms.ikms_mobile.data.NotificationsContract.PATH_NOTIFICATIONS;

import static com.pollub.ikms.ikms_mobile.data.SendersContract.PATH_SENDERS;

/**
 * Created by ATyKondziu on 29.11.2017.
 */

public class NotificationsProvider  extends ContentProvider {

    //constants for the operations
    private static final int NOTIFICATIONS = 1;
    private static final int NOTIFICATIONS_ID = 2;
    private static final int SENDERS = 3;
    private static final int SENDERS_ID = 4;

    //urimatcher
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //static initializer
    //All the code put here will execute the first time anything is called from this class
    static {
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_NOTIFICATIONS, NOTIFICATIONS);
        //single row of the table;  # - accepts any integer number
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_NOTIFICATIONS + "/#", NOTIFICATIONS_ID);

        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_SENDERS, SENDERS);
        //single row of the table;  # - accepts any integer number
        uriMatcher.addURI(CONTENT_AUTHORITY, PATH_SENDERS + "/#", SENDERS_ID);
    }

    private DBHelper helper;

    @Override
    public boolean onCreate() {
        helper = new DBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match) {
            case NOTIFICATIONS:
                cursor = db.query(NotificationsContract.NotificationsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
                break;
            case NOTIFICATIONS_ID:
                cursor = db.query(NotificationsContract.NotificationsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
                break;
            case SENDERS:
                cursor = db.query(SendersContract.SendersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
                break;
            case SENDERS_ID:
                cursor = db.query(SendersContract.SendersEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
                break;
            default:
                throw new IllegalArgumentException("Query unknown URI");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        int match = uriMatcher.match(uri);
        switch (match) {
            case NOTIFICATIONS:
                return insertRecord(uri, contentValues, NotificationsContract.NotificationsEntry.TABLE_NAME);
            case SENDERS:
                return insertRecord(uri, contentValues, SendersContract.SendersEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Insert unknown URI: " + uri);
        }
    }

    private Uri insertRecord(Uri uri, ContentValues values, String table) {
        SQLiteDatabase db = helper.getReadableDatabase();
        long id = db.insert(table, null, values);
        if (id == -1) {
            Log.e("NotificationsProvider", "Error insert " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case NOTIFICATIONS:
                return deleteRecord(uri, selection, selectionArgs, NotificationsContract.NotificationsEntry.TABLE_NAME);
            case NOTIFICATIONS_ID:
                return deleteRecord(uri, selection, selectionArgs, NotificationsContract.NotificationsEntry.TABLE_NAME);
            case SENDERS:
                return deleteRecord(uri, selection, selectionArgs, SendersContract.SendersEntry.TABLE_NAME);
            case SENDERS_ID:
                return deleteRecord(uri, selection, selectionArgs, SendersContract.SendersEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Delete unknown URI: " + uri);
        }
    }

    private int deleteRecord(Uri uri, String selection, String[] selectionArgs, String tableName) {
        SQLiteDatabase db = helper.getReadableDatabase();
        int id = db.delete(tableName, selection, selectionArgs);
        if (id == -1) {
            Log.e("NotificationsProvider", "Error delete " + uri);
            return -1;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match){
            case NOTIFICATIONS:
                return updateRecord(uri, values, selection, selectionArgs, NotificationsContract.NotificationsEntry.TABLE_NAME);
            case NOTIFICATIONS_ID:
                selection = NotificationsContract.NotificationsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateRecord(uri,values,selection,selectionArgs, NotificationsContract.NotificationsEntry.TABLE_NAME);
            case SENDERS:
                return updateRecord(uri, values, selection, selectionArgs, SendersContract.SendersEntry.TABLE_NAME);
            case SENDERS_ID:
                selection = SendersContract.SendersEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateRecord(uri,values,selection,selectionArgs, SendersContract.SendersEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Update unknown URI: " + uri);
        }
    }

    private int updateRecord(Uri uri, ContentValues values, String selection, String[] selectionArgs, String tableName) {
        SQLiteDatabase db = helper.getReadableDatabase();
        int id = db.update(tableName, values, selection, selectionArgs);
        if (id == 0) {
            Log.e("NotificationsProvider", "Error update" + uri);
            return -1;
        }
        return id;
    }
}
