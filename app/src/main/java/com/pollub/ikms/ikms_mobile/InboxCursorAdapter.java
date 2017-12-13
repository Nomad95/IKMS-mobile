package com.pollub.ikms.ikms_mobile;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.pollub.ikms.ikms_mobile.data.NotificationsContract;
import com.pollub.ikms.ikms_mobile.data.ReceivedMessagesContract;
import com.pollub.ikms.ikms_mobile.data.SendersContract;

/**
 * Created by ATyKondziu on 02.12.2017.
 */

public class InboxCursorAdapter extends CursorAdapter {
    public InboxCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public InboxCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(
                R.layout.item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //inbox - message - sender full name
        TextView senderFullNameTextView = (TextView) view.findViewById(R.id.inbox_item_from);
        int senderFullNameColumn = cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_SENDER_FULL_NAME);
        String inboxSenderFullNameText = cursor.getString(senderFullNameColumn);
        senderFullNameTextView.setText(inboxSenderFullNameText);
        //inbox - message - title
        TextView titleTextView = (TextView) view.findViewById(R.id.inbox_item_title);
        int titleColumn = cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_TITLE);
        String inboxTitleText = cursor.getString(titleColumn);
        titleTextView.setText(inboxTitleText);
        //inbox - message - date of send
        TextView dateOfSendTextView = (TextView) view.findViewById(R.id.inbox_item_date);
        int dateOfSendColumn = cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_DATE_OF_SEND);
        String inboxDateOfSendText = cursor.getString(dateOfSendColumn);
        dateOfSendTextView.setText(inboxDateOfSendText);

        int isRead = cursor.getInt(cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_WAS_READ));

        if(isRead == 1){
            view.setBackgroundResource(R.drawable.read_notification);
        }
        else
            view.setBackgroundResource(R.drawable.unread_notification);
    }
}
