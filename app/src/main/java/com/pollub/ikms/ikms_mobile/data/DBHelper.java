package com.pollub.ikms.ikms_mobile.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ATyKondziu on 29.11.2017.
 */
public class DBHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "notificationsDatabase.db";
    public final static int DATABASE_VERSION = 1;

    public final static String TABLE_NOTIFICATIONS_CREATE = "CREATE TABLE " + NotificationsContract.NotificationsEntry.TABLE_NAME + "("
            + NotificationsContract.NotificationsEntry._ID + " integer primary key autoincrement, "
            + NotificationsContract.NotificationsEntry.COLUMN_MANUFACTURER + " text not null,"
            + NotificationsContract.NotificationsEntry.COLUMN_MODEL + " text not null,"
            + NotificationsContract.NotificationsEntry.COLUMN_ANDROIDVERSION + " text, "
            + NotificationsContract.NotificationsEntry.COLUMN_WWW + " text);";

    private static final String TABLE_NOTOFICATIONS_DROP = "DROP TABLE IF EXISTS " + NotificationsContract.NotificationsEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //tworzenie bazy
        db.execSQL(TABLE_NOTIFICATIONS_CREATE);
        db.execSQL(TABLE_NOTIFICATIONS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //aktualizacja bazy do nowej wersji
        db.execSQL("DROP TABLE IF EXISTS " + NotificationsContract.NotificationsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + NotificationsContract.NotificationsEntry.TABLE_NAME);
        onCreate(db);
    }


    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Table Names
    private static final String TABLE_TODO = "todos";
    private static final String TABLE_TAG = "tags";
    private static final String TABLE_TODO_TAG = "todo_tags";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    // NOTES Table - column nmaes
    private static final String KEY_TODO = "todo";
    private static final String KEY_STATUS = "status";

    // TAGS Table - column names
    private static final String KEY_TAG_NAME = "tag_name";

    // NOTE_TAGS Table - column names
    private static final String KEY_TODO_ID = "todo_id";
    private static final String KEY_TAG_ID = "tag_id";

    // Table Create Statements
    // Todo table create statement
    private static final String CREATE_TABLE_TODO = "CREATE TABLE "
            + TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TODO
            + " TEXT," + KEY_STATUS + " INTEGER," + KEY_CREATED_AT
            + " DATETIME" + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_TAG
            + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TAG_NAME + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_TODO_TAG = "CREATE TABLE "
            + TABLE_TODO_TAG + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_TODO_ID + " INTEGER," + KEY_TAG_ID + " INTEGER,"
            + KEY_CREATED_AT + " DATETIME" + ")";

}
