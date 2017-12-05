package com.pollub.ikms.ikms_mobile.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ATyKondziu on 29.11.2017.
 */

public class SendersContract {
    //constants for Uri
    public static final String CONTENT_AUTHORITY = "com.pollub.ikms.ikms_mobile.notificationsprovider";
    public static final String PATH_SENDERS = "senders";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final class SendersEntry implements BaseColumns {
        public static final Uri SENDERS_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SENDERS);
        // Table name
        public final static String TABLE_NAME = "senders";
        //column (field) names
        public static final String _ID = BaseColumns._ID;
        public final static String COLUMN_SENDER_FULL_NAME = "sender_full_name";

    }
}