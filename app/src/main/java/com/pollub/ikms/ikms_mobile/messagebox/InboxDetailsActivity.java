package com.pollub.ikms.ikms_mobile.messagebox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.pollub.ikms.ikms_mobile.R;
import com.pollub.ikms.ikms_mobile.model.MessageItemModel;

public class InboxDetailsActivity extends AppCompatActivity {

    TextView from, dateOfSend, title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_details);

        from = (TextView) findViewById(R.id.inbox_details_from);
        dateOfSend = (TextView) findViewById(R.id.inbox_details_date);
        title = (TextView) findViewById(R.id.inbox_details_title);
        content = (TextView) findViewById(R.id.inbox_details_content);

        Bundle extras = getIntent().getExtras();
        Long messageId;

        if (extras != null) {
            messageId = extras.getLong("messageId");
            MessageItemModel message = findMessageById(messageId);

            from.setText(message.getRecipientFullName());
            dateOfSend.setText(message.getDateOfSend());
            title.setText(message.getTitle());
            content.setText(message.getMessageContents());
        } else {
            // TODO [Arek] wyrzucić użytkownikowi błąd i wywalić go do listy
        }
    }

    private MessageItemModel findMessageById(Long messageId){
        MessageItemModel messageItemModel1 = new MessageItemModel();
        messageItemModel1.setId(1L);
        messageItemModel1.setMessageContents("Treść Treść Treść Treść Treść ");
        messageItemModel1.setTitle("Pierwsza wiadomość wysłana");
        messageItemModel1.setRecipientFullName("Adrian Sarnecki");
        messageItemModel1.setWasRead(true);
        messageItemModel1.setDateOfSend("3:45");

        return messageItemModel1;
    }
}
