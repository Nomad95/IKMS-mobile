package com.pollub.ikms.ikms_mobile.messagebox;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pollub.ikms.ikms_mobile.R;
import com.pollub.ikms.ikms_mobile.model.MessageItemModel;

import java.util.ArrayList;

public class InboxListFragment extends Fragment {

    private InboxAdapter inboxAdapter;

    private ListView lv;

    private ArrayList<MessageItemModel> listOfMessageForListView = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox_list, container, false);

        MessageItemModel messageItemModel1 = new MessageItemModel();
        messageItemModel1.setId(1L);
        messageItemModel1.setMessageContents("abradab1");
        messageItemModel1.setTitle("Pierwsza wiadomość odebrana");
        messageItemModel1.setSenderFullName("Adrian Sarnecki");
        messageItemModel1.setSenderUsername("admin");
        messageItemModel1.setWasRead(true);
        messageItemModel1.setDateOfSend("3:45");

        MessageItemModel messageItemModel2 = new MessageItemModel();
        messageItemModel2.setId(2L);
        messageItemModel2.setMessageContents("abradabdasdasdasdasdasdasdasdsasd");
        messageItemModel2.setTitle("Druga wiadomość odebrana");
        messageItemModel2.setSenderFullName("Arek Boruch");
        messageItemModel2.setSenderUsername("user1");
        messageItemModel2.setWasRead(false);
        messageItemModel2.setDateOfSend("6.12.2017 3:45");

        MessageItemModel messageItemModel3 = new MessageItemModel();
        messageItemModel3.setId(3L);
        messageItemModel3.setMessageContents("abradab");
        messageItemModel3.setTitle("Trzecia wiadomość odebrana");
        messageItemModel3.setSenderFullName("Jadwiga Paździerz");
        messageItemModel3.setSenderUsername("admin");
        messageItemModel3.setWasRead(false);
        messageItemModel3.setDateOfSend("3.12.2017 4:00");

        MessageItemModel messageItemModel4 = new MessageItemModel();
        messageItemModel4.setId(4L);
        messageItemModel4.setMessageContents("dasdadassdasda");
        messageItemModel4.setTitle("Czwarta wiadomość odebrana");
        messageItemModel4.setSenderFullName("Ktośy Ktoś");
        messageItemModel4.setSenderUsername("user1");
        messageItemModel4.setWasRead(true);
        messageItemModel4.setDateOfSend("1.12.2017 20:45");

        listOfMessageForListView.add(messageItemModel1);
        listOfMessageForListView.add(messageItemModel2);
        listOfMessageForListView.add(messageItemModel3);
        listOfMessageForListView.add(messageItemModel4);


        lv = (ListView) rootView.findViewById(R.id.inbox_list);
        inboxAdapter = new InboxAdapter(getActivity(), 0, listOfMessageForListView);
        lv.setAdapter(inboxAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
             MessageItemModel selectedMessage = (MessageItemModel) adapterView.getItemAtPosition(position);
                Intent messageDetailsIntent = new Intent(getActivity(), InboxDetailsActivity.class);

                messageDetailsIntent.putExtra("messageId", selectedMessage.getId());
                messageDetailsIntent.putExtra("messageTitle", selectedMessage.getTitle());
                messageDetailsIntent.putExtra("messageSender", selectedMessage.getSenderFullName());
                messageDetailsIntent.putExtra("messageDateOfSend", selectedMessage.getDateOfSend());
                messageDetailsIntent.putExtra("messageContents", selectedMessage.getMessageContents());
                messageDetailsIntent.putExtra("messageSenderUsername", selectedMessage.getSenderUsername());
                startActivityForResult(messageDetailsIntent,1 );
            }
        });

        return rootView;
    }


}
