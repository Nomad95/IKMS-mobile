package com.pollub.ikms.ikms_mobile.data;

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

/**
 * Created by ATyKondziu on 29.11.2017.
 */

public class MessagesProvider extends ContentProvider {

    //constants for the operations
    private static final int RECEIVED_MESSAGES = 1;
    private static final int RECEIVED_MESSAGES_ID = 2;
    private static final int SENT_MESSAGES = 3;
    private static final int SENT_MESSAGES_ID = 4;

    private final String MY_QUERY = "SELECT m." + ReceivedMessagesContract.ReceivedMessagesEntry._ID +
            ", m." + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_WAS_READ +
            ", m." + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_SENDER_FULL_NAME +
            ", m." + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_TITLE +
            ", m." + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_DATE_OF_SEND +
            ", m." + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_MESSAGE_CONTENT +
            ", m." + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_DATE_OF_SEND +
            " FROM " + ReceivedMessagesContract.ReceivedMessagesEntry.TABLE_NAME + " m";


    //urimatcher
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //static initializer
    //All the code put here will execute the first time anything is called from this class
    static {
        uriMatcher.addURI(ReceivedMessagesContract.CONTENT_AUTHORITY, ReceivedMessagesContract.PATH_RECEIVED_MESSAGES, RECEIVED_MESSAGES);
        //single row of the table;  # - accepts any integer number
        uriMatcher.addURI(ReceivedMessagesContract.CONTENT_AUTHORITY, ReceivedMessagesContract.PATH_RECEIVED_MESSAGES + "/#", RECEIVED_MESSAGES_ID);

        uriMatcher.addURI(SentMessagesContract.CONTENT_AUTHORITY, SentMessagesContract.PATH_SENT_MESSAGES, SENT_MESSAGES);
        //single row of the table;  # - accepts any integer number
        uriMatcher.addURI(SentMessagesContract.CONTENT_AUTHORITY, SentMessagesContract.PATH_SENT_MESSAGES + "/#", SENT_MESSAGES_ID);
    }

    private DBHelper helper;

    @Override
    public boolean onCreate() {
        helper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String orderBy) {
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match) {
            case RECEIVED_MESSAGES:
                 cursor = db.rawQuery(MY_QUERY, new String[]{});
               // cursor = db.query(ReceivedMessagesContract.ReceivedMessagesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
               cursor.moveToFirst();
                Log.d("Cursor : ", cursor.toString());
                break;
            case RECEIVED_MESSAGES_ID:
                cursor = db.query(ReceivedMessagesContract.ReceivedMessagesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
                break;
            case SENT_MESSAGES:
                cursor = db.query(SentMessagesContract.SentMessagesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
                break;
            case SENT_MESSAGES_ID:
                cursor = db.query(SentMessagesContract.SentMessagesEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, orderBy);
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
            case RECEIVED_MESSAGES:
                return insertRecord(uri, contentValues, ReceivedMessagesContract.ReceivedMessagesEntry.TABLE_NAME);
            case SENT_MESSAGES:
                return insertRecord(uri, contentValues, SentMessagesContract.SentMessagesEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Insert unknown URI: " + uri);
        }
    }

    private Uri insertRecord(Uri uri, ContentValues values, String table) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long id = db.insert(table, null, values);
        if (id == -1) {
            Log.e("MessagesProvider", "Error insert " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case RECEIVED_MESSAGES:
                return deleteRecord(uri, selection, selectionArgs, ReceivedMessagesContract.ReceivedMessagesEntry.TABLE_NAME);
            case RECEIVED_MESSAGES_ID:
                return deleteRecord(uri, selection, selectionArgs, ReceivedMessagesContract.ReceivedMessagesEntry.TABLE_NAME);
            case SENT_MESSAGES:
                return deleteRecord(uri, selection, selectionArgs, SentMessagesContract.SentMessagesEntry.TABLE_NAME);
            case SENT_MESSAGES_ID:
                return deleteRecord(uri, selection, selectionArgs, SentMessagesContract.SentMessagesEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Delete unknown URI: " + uri);
        }
    }

    private int deleteRecord(Uri uri, String selection, String[] selectionArgs, String tableName) {
        SQLiteDatabase db = helper.getReadableDatabase();
        int id = db.delete(tableName, selection, selectionArgs);
        if (id == -1) {
            Log.e("MessagesProvider", "Error delete " + uri);
            return -1;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = uriMatcher.match(uri);
        switch (match) {
            case RECEIVED_MESSAGES:
                return updateRecord(uri, values, selection, selectionArgs, ReceivedMessagesContract.ReceivedMessagesEntry.TABLE_NAME);
            case RECEIVED_MESSAGES_ID:
                selection = ReceivedMessagesContract.ReceivedMessagesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateRecord(uri, values, selection, selectionArgs, ReceivedMessagesContract.ReceivedMessagesEntry.TABLE_NAME);
            case SENT_MESSAGES:
                return updateRecord(uri, values, selection, selectionArgs, SentMessagesContract.SentMessagesEntry.TABLE_NAME);
            case SENT_MESSAGES_ID:
                selection = SentMessagesContract.SentMessagesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateRecord(uri, values, selection, selectionArgs, SentMessagesContract.SentMessagesEntry.TABLE_NAME);
            default:
                throw new IllegalArgumentException("Update unknown URI: " + uri);
        }
    }

    private int updateRecord(Uri uri, ContentValues values, String selection, String[] selectionArgs, String tableName) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int id = db.update(tableName, values, selection, selectionArgs);
        if (id == 0) {
            Log.e("MessagesProvider", "Error update" + uri);
            return -1;
        }
        return id;
    }
}
