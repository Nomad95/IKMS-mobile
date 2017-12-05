package com.pollub.ikms.ikms_mobile;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.pollub.ikms.ikms_mobile.data.NotificationsContract;
import com.pollub.ikms.ikms_mobile.data.SendersContract;

/**
 * Created by ATyKondziu on 02.12.2017.
 */

public class NotificationsCursorAdapter extends CursorAdapter {
    public NotificationsCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public NotificationsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(
                R.layout.item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //Notification title - sender full name
        TextView senderFullNameTextView = (TextView) view.findViewById(R.id.notification_title);
        int senderFullNameColumn = cursor.getColumnIndex(SendersContract.SendersEntry.COLUMN_SENDER_FULL_NAME);
        String notificationTitleText = cursor.getString(senderFullNameColumn);
        senderFullNameTextView.setText(notificationTitleText);
        //Notification content
        TextView contentTextView = (TextView) view.findViewById(R.id.notification_content);
        int contentColumn = cursor.getColumnIndex(NotificationsContract.NotificationsEntry.COLUMN_CONTENT);
        String contentText = cursor.getString(contentColumn);
        contentTextView.setText(contentText);
    }
}
