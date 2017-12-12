package com.pollub.ikms.ikms_mobile.messagebox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.pollub.ikms.ikms_mobile.R;
import com.pollub.ikms.ikms_mobile.receiver.RequestResultReceiver;
import com.pollub.ikms.ikms_mobile.request.NewMessageRequest;
import com.pollub.ikms.ikms_mobile.service.SendingMessageService;

import static com.pollub.ikms.ikms_mobile.utils.constants.StatusCode.STATUS_ERROR;
import static com.pollub.ikms.ikms_mobile.utils.constants.StatusCode.STATUS_RUNNING;

public class SendingMessageActivity extends AppCompatActivity implements RequestResultReceiver.Receiver {

    private String token;

    private SharedPreferences prefs;

    private final String tokenKey = "com.pollub.ikms.ikms_mobile.token";

    private RequestResultReceiver receiver;

    private ProgressDialog progressDialog;

    private EditText recipientUsername, title;

    private MultiAutoCompleteTextView messageContents;

    private Button sendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_message);

        recipientUsername = (EditText) findViewById(R.id.new_message_recipient_username);
        title = (EditText) findViewById(R.id.new_message_title);
        messageContents = (MultiAutoCompleteTextView) findViewById(R.id.new_message_content);

        //todo [Arek] tego nie można tak zostawić ale jest 3 w nocy :(
        try{
            Bundle extras = getIntent().getExtras();
            String senderUsername = extras.getString("username");
            if(senderUsername != null){
                recipientUsername.setText(senderUsername);
            }
        }catch (Exception e){

        }


        sendButton = (Button) findViewById(R.id.send_new_msg);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewMessageRequest newMessageRequest = new NewMessageRequest();
                newMessageRequest.setRecipientUsername(recipientUsername.getText().toString());
                newMessageRequest.setTitle(title.getText().toString());
                newMessageRequest.setMessageContents(messageContents.getText().toString());

                startSendingMessage(newMessageRequest);
            }
        });


    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case STATUS_RUNNING:
                progressDialog = new ProgressDialog(SendingMessageActivity.this);
                // progressDialog.setTitle("ładowanie");
                progressDialog.setMessage("Ładowanie...");
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(true);
                progressDialog.show();
                break;
            case STATUS_ERROR:
                String statusCode = resultData.getString(Intent.EXTRA_TEXT);
                progressDialog.dismiss();
                Toast.makeText(this, "Niepoprawne dane logowania", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void startSendingMessage(NewMessageRequest newMessageRequest){
        prefs = this.getSharedPreferences(
                "com.pollub.ikms.ikms_mobile", Context.MODE_PRIVATE);
        token = prefs.getString(tokenKey, "");
        if (token.length() > 10) {

               /* Starting Login Service */
            receiver = new RequestResultReceiver(new Handler());
            receiver.setReceiver(SendingMessageActivity.this);
            Intent sendingNewMessageIntent = new Intent(Intent.ACTION_SYNC, null,
                    SendingMessageActivity.this, SendingMessageService.class);

                /* Send optional extras to Login IntentService */
            sendingNewMessageIntent.putExtra("receiver", receiver);
            sendingNewMessageIntent.putExtra("username", newMessageRequest.getRecipientUsername());
            sendingNewMessageIntent.putExtra("title", newMessageRequest.getTitle());
            sendingNewMessageIntent.putExtra("content", newMessageRequest.getMessageContents());

            startService(sendingNewMessageIntent);
        }
    }
}
