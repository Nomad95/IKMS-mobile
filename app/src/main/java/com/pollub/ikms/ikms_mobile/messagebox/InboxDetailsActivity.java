package com.pollub.ikms.ikms_mobile.messagebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pollub.ikms.ikms_mobile.R;
import com.pollub.ikms.ikms_mobile.model.MessageItemModel;
import com.pollub.ikms.ikms_mobile.request.NewMessageRequest;

public class InboxDetailsActivity extends AppCompatActivity {

    private TextView from, dateOfSend, title, content;
    private MessageItemModel message;
    private Button replayButton;

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

        replayButton = (Button) findViewById(R.id.replay);
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InboxDetailsActivity.this, SendingMessageActivity.class);
                intent.putExtra("username", message.getSenderUsername());
                startActivityForResult(intent, 1);
            }
        });
    }

    private MessageItemModel getMessageDetailsFromExtras() {

        Bundle extras = getIntent().getExtras();

        MessageItemModel messageItemModel1 = new MessageItemModel();
        messageItemModel1.setId(extras.getLong("messageId"));
        messageItemModel1.setMessageContents(extras.getString("messageContents"));
        messageItemModel1.setTitle(extras.getString("messageTitle"));
        messageItemModel1.setSenderFullName(extras.getString("messageSender"));
        messageItemModel1.setDateOfSend(extras.getString("messageDateOfSend"));
        messageItemModel1.setSenderUsername(extras.getString("messageSenderUsername"));

        return messageItemModel1;
    }
}
