package com.pollub.ikms.ikms_mobile.messagebox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.pollub.ikms.ikms_mobile.R;

public class OutboxDetailsActivity extends AppCompatActivity {

    TextView tvIdMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_details);

        //tvIdMessage = (TextView) findViewById(R.id.messageId);

        Bundle extras = getIntent().getExtras();
        Long messageId;

        if (extras != null) {
            messageId = extras.getLong("messageId");
            tvIdMessage.setText(String.valueOf(messageId));
        }


    }
}
