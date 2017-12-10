package com.pollub.ikms.ikms_mobile.messagebox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pollub.ikms.ikms_mobile.R;
import com.pollub.ikms.ikms_mobile.model.MessageItemModel;

public class InboxDetailsActivity extends AppCompatActivity {

    TextView from, dateOfSend, title, content;
    MessageItemModel message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_details);

        from = (TextView) findViewById(R.id.inbox_details_from);
        dateOfSend = (TextView) findViewById(R.id.inbox_details_date);
        title = (TextView) findViewById(R.id.inbox_details_title);
        content = (TextView) findViewById(R.id.inbox_details_content);

        message = getMessageDetailsFromExtras();

        from.setText(message.getSenderFullName());
        dateOfSend.setText(message.getDateOfSend());
        title.setText(message.getTitle());
        content.setText(message.getMessageContents());
    }

    private MessageItemModel getMessageDetailsFromExtras() {

        Bundle extras = getIntent().getExtras();

        MessageItemModel messageItemModel1 = new MessageItemModel();
        messageItemModel1.setId(extras.getLong("messageId"));
        messageItemModel1.setMessageContents(extras.getString("messageContents"));
        messageItemModel1.setTitle(extras.getString("messageTitle"));
        messageItemModel1.setSenderFullName(extras.getString("messageSender"));
        messageItemModel1.setDateOfSend(extras.getString("messageDateOfSend"));
        messageItemModel1.setSenderUsername(extras.getString("senderUsername"));

        return messageItemModel1;
    }
}
