package com.pollub.ikms.ikms_mobile.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ATyKondziu on 29.11.2017.
 */

public class NotificationsContract {
    //constants for Uri
    public static final String CONTENT_AUTHORITY = "com.pollub.ikms.ikms_mobile.phonesprovider";
    public static final String PATH_NOTIFICATIONS = "notifications";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final class NotificationsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTIFICATIONS);
        // Table name
        public final static String TABLE_NAME = "notifications";
        //column (field) names
        public static final String _ID = BaseColumns._ID;
        public final static String COLUMN_MANUFACTURER = "manufacturer";
        public final static String COLUMN_MODEL = "model";
        public final static String COLUMN_ANDROIDVERSION = "android_version";
        public final static String COLUMN_WWW = "www";

    }
}