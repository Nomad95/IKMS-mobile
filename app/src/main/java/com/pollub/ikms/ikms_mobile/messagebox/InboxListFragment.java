package com.pollub.ikms.ikms_mobile.messagebox;


import android.content.Intent;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import com.pollub.ikms.ikms_mobile.InboxCursorAdapter;
import com.pollub.ikms.ikms_mobile.R;
import com.pollub.ikms.ikms_mobile.data.ReceivedMessagesContract;
import com.pollub.ikms.ikms_mobile.model.MessageItemModel;

public class InboxListFragment extends Fragment implements
        LoaderCallbacks<Cursor> {

    private InboxCursorAdapter inboxCursorAdapter;

    private ListView lv;

    private Cursor cursor;

    private static final int URL_LOADER = 0;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_inbox_list, container, false);


        getLoaderManager().initLoader(URL_LOADER, null, this);
        //ListView - set adapter
        lv = (ListView) rootView.findViewById(R.id.inbox_list);

        inboxCursorAdapter = new InboxCursorAdapter(rootView.getContext(), cursor, false);

        lv.setAdapter(inboxCursorAdapter);

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


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_SENDER_FULL_NAME,
                ReceivedMessagesContract.ReceivedMessagesEntry.TABLE_NAME + "." + ReceivedMessagesContract.ReceivedMessagesEntry._ID,
                ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_WAS_READ,
                ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_TITLE,
                ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_DATE_OF_SEND};
        return  new CursorLoader(
                getActivity(),
                ReceivedMessagesContract.ReceivedMessagesEntry.RECEIVED_MESSAGES_URI,
                projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {

    }
}
