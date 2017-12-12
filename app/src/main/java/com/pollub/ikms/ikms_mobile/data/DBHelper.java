package com.pollub.ikms.ikms_mobile.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pollub.ikms.ikms_mobile.model.NotificationItemModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by ATyKondziu on 29.11.2017.
 */
public class DBHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public final static String DATABASE_NAME = "notificationsDatabase.db";
    public final static int DATABASE_VERSION = 1;

    public final static String TABLE_NOTIFICATIONS_CREATE = "CREATE TABLE " + NotificationsContract.NotificationsEntry.TABLE_NAME + "("
            + NotificationsContract.NotificationsEntry._ID + " integer primary key, "
            + NotificationsContract.NotificationsEntry.COLUMN_CONTENT + " text not null,"
            + NotificationsContract.NotificationsEntry.COLUMN_DATE_OF_SEND + " datetime not null,"
            + NotificationsContract.NotificationsEntry.COLUMN_PRIORITY + " text, "
            + NotificationsContract.NotificationsEntry.COLUMN_WAS_READ + " integer, "
            + NotificationsContract.NotificationsEntry.COLUMN_SENDER_ID_COLUMN + " text);";

    public final static String TABLE_SENDERS_CREATE = "CREATE TABLE " + SendersContract.SendersEntry.TABLE_NAME + "("
            + SendersContract.SendersEntry._ID + " integer primary key, "
            + SendersContract.SendersEntry.COLUMN_SENDER_FULL_NAME + " text not null);";

    public final static String TABLE_RECEIVED_MESSAGES_CREATE = "CREATE TABLE " + ReceivedMessagesContract.ReceivedMessagesEntry.TABLE_NAME + "("
            + ReceivedMessagesContract.ReceivedMessagesEntry._ID + " integer primary key, "
            + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_SENDER_ID + " integer not null,"
            + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_TITLE + " text not null,"
            + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_RECIPIENT_ID + " integer not null,"
            + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_DATE_OF_SEND + " datetime not null,"
            + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_MESSAGE_CONTENT + " text, "
            + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_WAS_READ + " text, "
            + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_RECIPIENT_USERNAME + " text"
            + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_SENDER_USERNAME + " text"
            + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_RECIPIENT_FULL_NAME + " text"
            + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_SENDER_FULL_NAME + " text);";

    public final static String TABLE_SENT_MESSAGES_CREATE = "CREATE TABLE " + SentMessagesContract.SentMessagesEntry.TABLE_NAME + "("
            + SentMessagesContract.SentMessagesEntry._ID + " integer primary key, "
            + SentMessagesContract.SentMessagesEntry.COLUMN_SENDER_ID + " integer not null,"
            + SentMessagesContract.SentMessagesEntry.COLUMN_TITLE + " text not null,"
            + SentMessagesContract.SentMessagesEntry.COLUMN_RECIPIENT_ID + " integer not null,"
            + SentMessagesContract.SentMessagesEntry.COLUMN_DATE_OF_SEND + " datetime not null,"
            + SentMessagesContract.SentMessagesEntry.COLUMN_MESSAGE_CONTENT + " text, "
            + SentMessagesContract.SentMessagesEntry.COLUMN_WAS_READ + " integer, "
            + SentMessagesContract.SentMessagesEntry.COLUMN_RECIPIENT_USERNAME + " text"
            + SentMessagesContract.SentMessagesEntry.COLUMN_SENDER_USERNAME + " text"
            + SentMessagesContract.SentMessagesEntry.COLUMN_RECIPIENT_FULL_NAME + " text"
            + SentMessagesContract.SentMessagesEntry.COLUMN_SENDER_FULL_NAME + " text);";

    private static final String TABLE_SENDERS_DROP = "DROP TABLE IF EXISTS " + SendersContract.SendersEntry.TABLE_NAME;

    private static final String TABLE_NOTIFICATIONS_DROP = "DROP TABLE IF EXISTS " + NotificationsContract.NotificationsEntry.TABLE_NAME;

    private static final String TABLE_RECEIVED_MESSAGES_DROP = "DROP TABLE IF EXISTS " + ReceivedMessagesContract.ReceivedMessagesEntry.TABLE_NAME;

    private static final String TABLE_SENT_MESSAGES_DROP = "DROP TABLE IF EXISTS " + SentMessagesContract.SentMessagesEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //tworzenie bazy
        db.execSQL(TABLE_SENDERS_CREATE);
        db.execSQL(TABLE_NOTIFICATIONS_CREATE);
        db.execSQL(TABLE_RECEIVED_MESSAGES_CREATE);
        db.execSQL(TABLE_SENT_MESSAGES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //aktualizacja bazy do nowej wersji
        db.execSQL(TABLE_SENDERS_DROP);
        db.execSQL(TABLE_NOTIFICATIONS_DROP);
        db.execSQL(TABLE_RECEIVED_MESSAGES_DROP);
        db.execSQL(TABLE_SENT_MESSAGES_DROP);
        onCreate(db);
    }

    public ArrayList<NotificationItemModel> getAllNotifications(){
        return new ArrayList<>();
    }
}
