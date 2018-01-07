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
        messageItemModel1.setMessageContents("Witam, \nPaństwa córce będzie potrzebne kilka zajęć w związku ze zdiagnozowaną na ostatnich badanich drobną dysfunkcję. \n\nPo więcej informacji zapraszam na konsultacje w czwartki w godzinach 15-16. \n \nJagoda Wilk - logopeda ");
        messageItemModel1.setTitle("Dodatkowe zajęcia - dogoterapia ");
        messageItemModel1.setSenderFullName("Jagoda Wilk");
        messageItemModel1.setSenderUsername("admin");
        messageItemModel1.setWasRead(true);
        messageItemModel1.setDateOfSend("11:45");

        MessageItemModel messageItemModel2 = new MessageItemModel();
        messageItemModel2.setId(2L);
        messageItemModel2.setMessageContents("abradabdasdasdasdasdasdasdasdsasd");
        messageItemModel2.setTitle("Zajęcia na świeżym powietrzu");
        messageItemModel2.setSenderFullName("Katarzyna Piórko");
        messageItemModel2.setSenderUsername("user1");
        messageItemModel2.setWasRead(false);
        messageItemModel2.setDateOfSend("06.01.2017 16:45");

        MessageItemModel messageItemModel3 = new MessageItemModel();
        messageItemModel3.setId(3L);
        messageItemModel3.setMessageContents("abradab");
        messageItemModel3.setTitle("Uwagi dotyczące zmiany planu terapii");
        messageItemModel3.setSenderFullName("Arkadiusz Miłoszewski");
        messageItemModel3.setSenderUsername("admin");
        messageItemModel3.setWasRead(false);
        messageItemModel3.setDateOfSend("06.01.2018 16:01");

        MessageItemModel messageItemModel4 = new MessageItemModel();
        messageItemModel4.setId(4L);
        messageItemModel4.setMessageContents("dasdadassdasda");
        messageItemModel4.setTitle("Dokumenty na konsultacje");
        messageItemModel4.setSenderFullName("Irmina Patios");
        messageItemModel4.setSenderUsername("user2");
        messageItemModel4.setWasRead(true);
        messageItemModel4.setDateOfSend("05.01.2018 9:22");

        MessageItemModel messageItemModel5 = new MessageItemModel();
        messageItemModel5.setId(4L);
        messageItemModel5.setMessageContents("dasdadassdasda");
        messageItemModel5.setTitle("Uwagi dotyczące rozwoju dziecka");
        messageItemModel5.setSenderFullName("Elzbieta Kawka");
        messageItemModel5.setSenderUsername("user3");
        messageItemModel5.setWasRead(true);
        messageItemModel5.setDateOfSend("04.01.2018 10:11");

        MessageItemModel messageItemModel6 = new MessageItemModel();
        messageItemModel6.setId(4L);
        messageItemModel6.setMessageContents("dasdadassdasda");
        messageItemModel6.setTitle("Materiały dydaktyczne do domu");
        messageItemModel6.setSenderFullName("Marek Berk");
        messageItemModel6.setSenderUsername("user4");
        messageItemModel6.setWasRead(true);
        messageItemModel6.setDateOfSend("03.01.2018 15:20");

        MessageItemModel messageItemModel7 = new MessageItemModel();
        messageItemModel7.setId(5L);
        messageItemModel7.setMessageContents("dasdadassdasda");
        messageItemModel7.setTitle("Wiadomość powitalna");
        messageItemModel7.setSenderFullName("Joanna Zielińska");
        messageItemModel7.setSenderUsername("admin");
        messageItemModel7.setWasRead(true);
        messageItemModel7.setDateOfSend("03.01.2018 20:45");

        listOfMessageForListView.add(messageItemModel1);
        listOfMessageForListView.add(messageItemModel2);
        listOfMessageForListView.add(messageItemModel3);
        listOfMessageForListView.add(messageItemModel4);
        listOfMessageForListView.add(messageItemModel5);
        listOfMessageForListView.add(messageItemModel6);
        listOfMessageForListView.add(messageItemModel7);


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
