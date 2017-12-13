package com.pollub.ikms.ikms_mobile.messagebox;

import android.content.Intent;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import com.pollub.ikms.ikms_mobile.OutboxCursorAdapter;
import com.pollub.ikms.ikms_mobile.R;
import com.pollub.ikms.ikms_mobile.data.ReceivedMessagesContract;
import com.pollub.ikms.ikms_mobile.data.SentMessagesContract;
import com.pollub.ikms.ikms_mobile.model.MessageItemModel;

public class OutboxListFragment extends Fragment implements
       LoaderCallbacks<Cursor> {

    private OutboxCursorAdapter outboxCursorAdapter;

    private ListView lv;

    private Cursor cursor;

    private static final int URL_LOADER = 0;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_outbox_list, container, false);


        getLoaderManager().initLoader(URL_LOADER, null, this);
        //ListView - set adapter
        lv = (ListView) rootView.findViewById(R.id.outbox_list);

        outboxCursorAdapter = new OutboxCursorAdapter(rootView.getContext(), cursor, false);

        lv.setAdapter(outboxCursorAdapter);

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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {SentMessagesContract.SentMessagesEntry.COLUMN_SENDER_FULL_NAME,
                SentMessagesContract.SentMessagesEntry.TABLE_NAME + "." + SentMessagesContract.SentMessagesEntry._ID,
                SentMessagesContract.SentMessagesEntry.COLUMN_WAS_READ,
                SentMessagesContract.SentMessagesEntry.COLUMN_TITLE,
                SentMessagesContract.SentMessagesEntry.COLUMN_DATE_OF_SEND};
        return  new CursorLoader(
                getActivity(),
                SentMessagesContract.SentMessagesEntry.SENT_MESSAGES_URI,
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
