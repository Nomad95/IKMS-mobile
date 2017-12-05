package com.pollub.ikms.ikms_mobile.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
            + NotificationsContract.NotificationsEntry.COLUMN_WAS_READ + " text, "
            + NotificationsContract.NotificationsEntry.COLUMN_SENDER_ID_COLUMN + " text);";

    public final static String TABLE_SENDERS_CREATE = "CREATE TABLE " + SendersContract.SendersEntry.TABLE_NAME + "("
            + SendersContract.SendersEntry._ID + " integer primary key, "
            + SendersContract.SendersEntry.COLUMN_SENDER_FULL_NAME + " text not null);";

    public final static String TABLE_MESSAGES_CREATE = "CREATE TABLE " + MessagesContract.MessagesEntry.TABLE_NAME + "("
            + MessagesContract.MessagesEntry._ID + " integer primary key, "
            + MessagesContract.MessagesEntry.COLUMN_SENDER_ID + " integer not null,"
            + MessagesContract.MessagesEntry.COLUMN_TITLE + " text not null,"
            + MessagesContract.MessagesEntry.COLUMN_RECIPIENT_ID + " integer not null,"
            + MessagesContract.MessagesEntry.COLUMN_DATE_OF_SEND + " datetime not null,"
            + MessagesContract.MessagesEntry.COLUMN_MESSAGE_CONTENT + " text, "
            + MessagesContract.MessagesEntry.COLUMN_WAS_READ + " text, "
            + MessagesContract.MessagesEntry.COLUMN_RECIPIENT_USERNAME + " text"
            + MessagesContract.MessagesEntry.COLUMN_SENDER_USERNAME + " text"
            + MessagesContract.MessagesEntry.COLUMN_RECIPIENT_FULL_NAME + " text"
            + MessagesContract.MessagesEntry.COLUMN_SENDER_FULL_NAME + " text"
            + MessagesContract.MessagesEntry.COLUMN_NUMBER_OF_UNREAD + " text";


    private static final String TABLE_SENDERS_DROP = "DROP TABLE IF EXISTS " + SendersContract.SendersEntry.TABLE_NAME;

    private static final String TABLE_NOTOFICATIONS_DROP = "DROP TABLE IF EXISTS " + NotificationsContract.NotificationsEntry.TABLE_NAME;

    private static final String TABLE_MESSAGES_DROP = "DROP TABLE IF EXISTS " + MessagesContract.MessagesEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //tworzenie bazy
        db.execSQL(TABLE_SENDERS_CREATE);
        db.execSQL(TABLE_NOTIFICATIONS_CREATE);
        db.execSQL(TABLE_MESSAGES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //aktualizacja bazy do nowej wersji
        db.execSQL("DROP TABLE IF EXISTS " + NotificationsContract.NotificationsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SendersContract.SendersEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MessagesContract.MessagesEntry.TABLE_NAME);
        onCreate(db);
    }
}
