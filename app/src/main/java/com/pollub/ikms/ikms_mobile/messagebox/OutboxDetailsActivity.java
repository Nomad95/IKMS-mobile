package com.pollub.ikms.ikms_mobile.messagebox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.pollub.ikms.ikms_mobile.R;
import com.pollub.ikms.ikms_mobile.model.MessageItemModel;

public class OutboxDetailsActivity extends AppCompatActivity {

    TextView to, dateOfSend, title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outbox_details);

        to = (TextView) findViewById(R.id.outbox_details_to);
        dateOfSend = (TextView) findViewById(R.id.outbox_details_date);
        title = (TextView) findViewById(R.id.outbox_details_title);
        content = (TextView) findViewById(R.id.outbox_details_content);

        MessageItemModel message = getMessageDetailsFromExtras();

        to.setText(message.getRecipientFullName());
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
        messageItemModel1.setRecipientFullName(extras.getString("messageRecipient"));
        messageItemModel1.setDateOfSend(extras.getString("messageDateOfSend"));

        return messageItemModel1;
    }
}
