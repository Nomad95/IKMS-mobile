package com.pollub.ikms.ikms_mobile.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ATyKondziu on 29.11.2017.
 */

public class NotificationsContract {
    //constants for Uri
    public static final String CONTENT_AUTHORITY = "com.pollub.ikms.ikms_mobile.notificationsprovider";
    public static final String PATH_NOTIFICATIONS = "notifications";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final class NotificationsEntry implements BaseColumns {

        public static final Uri NOTIFICATIONS_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTIFICATIONS);

        // Table name
        public final static String TABLE_NAME = "notifications";

        //column (field) names
        public static final String _ID = BaseColumns._ID;
        public final static String COLUMN_CONTENT = "content";
        public final static String COLUMN_DATE_OF_SEND= "date_of_send";
        public final static String COLUMN_WAS_READ= "was_read";
        public final static String COLUMN_PRIORITY= "priority";
        public final static String COLUMN_SENDER_ID_COLUMN= "sender_id";

        public static Uri TableAWithTABLEB() {
            return NOTIFICATIONS_URI.buildUpon().appendPath(SendersContract.PATH_SENDERS).build();
        }
    }
}