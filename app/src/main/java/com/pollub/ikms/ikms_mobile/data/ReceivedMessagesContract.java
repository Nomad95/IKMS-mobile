package com.pollub.ikms.ikms_mobile.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ATyKondziu on 29.11.2017.
 */

public class ReceivedMessagesContract {
    //constants for Uri
    public static final String CONTENT_AUTHORITY = "com.pollub.ikms.ikms_mobile.messagesprovider";
    public static final String PATH_RECEIVED_MESSAGES = "received_messages";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final class ReceivedMessagesEntry implements BaseColumns {

        public static final Uri RECEIVED_MESSAGES_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RECEIVED_MESSAGES);

        // Table name
        public final static String TABLE_NAME = "received_messages";

        //column (field) names
        public static final String _ID = BaseColumns._ID;
        public final static String COLUMN_SENDER_ID = "sender_id";
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_RECIPIENT_ID = "recipient_id";
        public final static String COLUMN_DATE_OF_SEND = "date_of_send";
        public final static String COLUMN_MESSAGE_CONTENT = "message_content";
        public final static String COLUMN_WAS_READ = "was_read";
        public final static String COLUMN_RECIPIENT_USERNAME = "recipient_username";
        public final static String COLUMN_SENDER_USERNAME = "sender_username";
        public final static String COLUMN_RECIPIENT_FULL_NAME = "recipient_full_name";
        public final static String COLUMN_SENDER_FULL_NAME = "sender_full_name";
        public final static String COLUMN_NUMBER_OF_UNREAD = "number_of_unread";
    }
}