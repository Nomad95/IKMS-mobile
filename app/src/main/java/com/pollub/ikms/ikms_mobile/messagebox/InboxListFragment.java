package com.pollub.ikms.ikms_mobile.messagebox;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pollub.ikms.ikms_mobile.R;
import com.pollub.ikms.ikms_mobile.data.DatabaseManager;
import com.pollub.ikms.ikms_mobile.data.ReceivedMessagesContract;
import com.pollub.ikms.ikms_mobile.model.MessageItemModel;

import java.util.ArrayList;

public class InboxListFragment extends Fragment  {

    private InboxAdapter inboxAdapter;

    private ListView lv;

    private Cursor cursor;

    private static final int URL_LOADER = 0;

    private View rootView;
    private ArrayList<MessageItemModel> listOfMessageForListView = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_inbox_list, container, false);

        lv = (ListView) rootView.findViewById(R.id.inbox_list);

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        cursor = db.rawQuery(MY_QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                MessageItemModel messageItemModel1 = new MessageItemModel();
                messageItemModel1.setId(cursor.getLong(cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry._ID)));
                messageItemModel1.setMessageContents(cursor.getString(cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_MESSAGE_CONTENT)));
                messageItemModel1.setTitle(cursor.getString(cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_TITLE)));
                messageItemModel1.setSenderFullName(cursor.getString(cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_SENDER_FULL_NAME)));
                messageItemModel1.setSenderUsername(cursor.getString(cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_SENDER_USERNAME)));
                messageItemModel1.setWasRead(cursor.getInt(cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_WAS_READ))==1);
                messageItemModel1.setDateOfSend(cursor.getString(cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_DATE_OF_SEND)));
                listOfMessageForListView.add(messageItemModel1);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        inboxAdapter = new InboxAdapter(getActivity(), 0, listOfMessageForListView);
        lv.setAdapter(inboxAdapter);
        lv.setOnItemClickListener((adapterView, view, position, id) -> {
         MessageItemModel selectedMessage = (MessageItemModel) adapterView.getItemAtPosition(position);
            Intent messageDetailsIntent = new Intent(getActivity(), InboxDetailsActivity.class);

            messageDetailsIntent.putExtra("messageId", selectedMessage.getId());
            messageDetailsIntent.putExtra("messageTitle", selectedMessage.getTitle());
            messageDetailsIntent.putExtra("messageSender", selectedMessage.getSenderFullName());
            messageDetailsIntent.putExtra("messageDateOfSend", selectedMessage.getDateOfSend());
            messageDetailsIntent.putExtra("messageContents", selectedMessage.getMessageContents());
            messageDetailsIntent.putExtra("messageSenderUsername", selectedMessage.getSenderUsername());
            startActivityForResult(messageDetailsIntent,1 );
        });

        return rootView;
    }

    private final String MY_QUERY = "SELECT m." + ReceivedMessagesContract.ReceivedMessagesEntry._ID +
            ", m." + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_MESSAGE_CONTENT +
            ", m." + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_TITLE +
            ", m." + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_SENDER_FULL_NAME +
            ", m." + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_SENDER_USERNAME +
            ", m." + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_WAS_READ +
            ", m." + ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_DATE_OF_SEND +
            " FROM " + ReceivedMessagesContract.ReceivedMessagesEntry.TABLE_NAME + " m";

}
