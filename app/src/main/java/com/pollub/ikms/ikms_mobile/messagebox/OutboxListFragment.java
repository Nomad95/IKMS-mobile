package com.pollub.ikms.ikms_mobile.messagebox;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pollub.ikms.ikms_mobile.R;
import com.pollub.ikms.ikms_mobile.data.DatabaseManager;
import com.pollub.ikms.ikms_mobile.data.ReceivedMessagesContract;
import com.pollub.ikms.ikms_mobile.data.SentMessagesContract;
import com.pollub.ikms.ikms_mobile.model.MessageItemModel;

import java.util.ArrayList;

public class OutboxListFragment extends Fragment {

    private OutboxAdapter outboxAdapter;

    private Cursor cursor;

    private ListView lv;

    private ArrayList<MessageItemModel> listOfMessageForListView = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_outbox_list, container, false);

        MessageItemModel messageItemModel4 = new MessageItemModel();
        messageItemModel4.setId(4L);
        messageItemModel4.setMessageContents("dasdadassdasda");
        messageItemModel4.setTitle("Dokumenty na konsultacje");
        messageItemModel4.setRecipientFullName("Irmina Patios");
        messageItemModel4.setRecipientUsername("user2");
        messageItemModel4.setWasRead(true);
        messageItemModel4.setDateOfSend("05.01.2018 10:22");

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        cursor = db.rawQuery(MY_QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                MessageItemModel messageItemModel1 = new MessageItemModel();
                messageItemModel1.setId(cursor.getLong(cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry._ID)));
                messageItemModel1.setMessageContents(cursor.getString(cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_MESSAGE_CONTENT)));
                messageItemModel1.setTitle(cursor.getString(cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_TITLE)));
                messageItemModel1.setRecipientFullName(cursor.getString(cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_RECIPIENT_FULL_NAME)));
                messageItemModel1.setRecipientUsername(cursor.getString(cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_RECIPIENT_USERNAME)));
                messageItemModel1.setWasRead(cursor.getInt(cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_WAS_READ))==1);
                messageItemModel1.setDateOfSend(cursor.getString(cursor.getColumnIndex(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_DATE_OF_SEND)));
                listOfMessageForListView.add(messageItemModel1);
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
        lv = (ListView) rootView.findViewById(R.id.outbox_list);
        outboxAdapter = new OutboxAdapter(getActivity(), 0, listOfMessageForListView);
        lv.setAdapter(outboxAdapter);

        lv.setOnItemClickListener((adapterView, view, position, id) -> {
            MessageItemModel selectedMessage = (MessageItemModel) adapterView.getItemAtPosition(position);
            Intent messageDetailsIntent = new Intent(getActivity(), OutboxDetailsActivity.class);

            messageDetailsIntent.putExtra("messageId", selectedMessage.getId());
            messageDetailsIntent.putExtra("messageTitle", selectedMessage.getTitle());
            messageDetailsIntent.putExtra("messageRecipient", selectedMessage.getRecipientFullName());
            messageDetailsIntent.putExtra("messageDateOfSend", selectedMessage.getDateOfSend());
            messageDetailsIntent.putExtra("messageContents", selectedMessage.getMessageContents());
            startActivityForResult(messageDetailsIntent,1 );
        });

        return rootView;
    }

    private final String MY_QUERY = "SELECT m." + SentMessagesContract.SentMessagesEntry._ID +
            ", m." + SentMessagesContract.SentMessagesEntry.COLUMN_MESSAGE_CONTENT +
            ", m." + SentMessagesContract.SentMessagesEntry.COLUMN_TITLE +
            ", m." + SentMessagesContract.SentMessagesEntry.COLUMN_RECIPIENT_FULL_NAME +
            ", m." + SentMessagesContract.SentMessagesEntry.COLUMN_RECIPIENT_USERNAME +
            ", m." + SentMessagesContract.SentMessagesEntry.COLUMN_WAS_READ +
            ", m." + SentMessagesContract.SentMessagesEntry.COLUMN_DATE_OF_SEND +
            " FROM " + SentMessagesContract.SentMessagesEntry.TABLE_NAME + " m";

}
