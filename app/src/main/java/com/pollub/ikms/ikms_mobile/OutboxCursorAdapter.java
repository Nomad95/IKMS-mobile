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
import com.pollub.ikms.ikms_mobile.data.SentMessagesContract;

/**
 * Created by ATyKondziu on 02.12.2017.
 */

public class OutboxCursorAdapter extends CursorAdapter {
    public OutboxCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public OutboxCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(
                R.layout.item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //outbox - message - recipient full name
        TextView recipientFullNameTextView = (TextView) view.findViewById(R.id.outbox_item_to);
        int recipientFullNameColumn = cursor.getColumnIndex(SentMessagesContract.SentMessagesEntry.COLUMN_RECIPIENT_FULL_NAME);
        String outboxSenderFullNameText = cursor.getString(recipientFullNameColumn);
        recipientFullNameTextView.setText(outboxSenderFullNameText);
        //outbox - message - title
        TextView titleTextView = (TextView) view.findViewById(R.id.outbox_item_title);
        int titleColumn = cursor.getColumnIndex(SentMessagesContract.SentMessagesEntry.COLUMN_TITLE);
        String outboxTitleText = cursor.getString(titleColumn);
        titleTextView.setText(outboxTitleText);
        //outbox - message - date of send
        TextView dateOfSendTextView = (TextView) view.findViewById(R.id.outbox_item_date);
        int dateOfSendColumn = cursor.getColumnIndex(SentMessagesContract.SentMessagesEntry.COLUMN_DATE_OF_SEND);
        String outboxDateOfSendText = cursor.getString(dateOfSendColumn);
        dateOfSendTextView.setText(outboxDateOfSendText);

        int isRead = cursor.getInt(cursor.getColumnIndex(SentMessagesContract.SentMessagesEntry.COLUMN_WAS_READ));

        if(isRead == 1){
            view.setBackgroundResource(R.drawable.read_notification);
        }
        else
            view.setBackgroundResource(R.drawable.unread_notification);
    }
}
