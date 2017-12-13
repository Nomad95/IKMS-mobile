package com.pollub.ikms.ikms_mobile.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.pollub.ikms.ikms_mobile.data.MessagesQueryHandler;
import com.pollub.ikms.ikms_mobile.data.ReceivedMessagesContract;
import com.pollub.ikms.ikms_mobile.data.SendersContract;
import com.pollub.ikms.ikms_mobile.data.SentMessagesContract;
import com.pollub.ikms.ikms_mobile.response.MessageResponse;
import com.pollub.ikms.ikms_mobile.utils.UrlManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by ATyKondziu on 18.11.2017.
 */

public class FetchMessagesIntentService extends IntentService {
    public static final int STATUS_RUNNING = 3;
    public static final int STATUS_FINISHED = 4;
    public static final int STATUS_ERROR = 5;

    private static final String TAG = "MessagesService";

    private String token;

    private SharedPreferences prefs;
    private String tokenKey = "com.pollub.ikms.ikms_mobile.token";

    private MessageResponse[] receivedMessages;

    private MessageResponse[] sentMessages;

    private static final MessageResponse[] NO_MESSAGES = {};

    private ResponseEntity<MessageResponse[]> response;

    private MessagesQueryHandler handler;

    public FetchMessagesIntentService() {
        super(TAG);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "FetchMessagesIntentService Started!");
        prefs = this.getSharedPreferences(
                "com.pollub.ikms.ikms_mobile", Context.MODE_PRIVATE);
        token = prefs.getString(tokenKey, "");
        handler = new MessagesQueryHandler(getContentResolver());

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle messagesBundle = new Bundle();
        receiver.send(STATUS_RUNNING, messagesBundle);
        try {
            receivedMessages = getReceivedMessages();
            saveReceivedMessagesToSQLITE(receivedMessages);
            sentMessages = getSentMessages();
            saveSentMessagesToSQLITE(sentMessages);
            if (receivedMessages.length > 0 && sentMessages.length > 0) {
                messagesBundle.putInt("unreadMessages", countAllUnreadMessages());
                receiver.send(STATUS_FINISHED, messagesBundle);
            } else {
                receiver.send(STATUS_FINISHED, messagesBundle);
            }
        } catch (Exception e) {
            messagesBundle.putString(Intent.EXTRA_TEXT, e.getMessage());
            receiver.send(STATUS_ERROR, messagesBundle);
        }

        Log.d(TAG, "FetchMessagesIntentService Stopping!");
        // this.stopSelf();
    }

    private int countAllUnreadMessages() {
        int counter = 0;
        for (MessageResponse receivedMessage : receivedMessages) {
            if (receivedMessage.getWasRead()) {
                 counter ++;
            }
        }
        return counter;
    }


    private MessageResponse[] getReceivedMessages() {
        final String url = UrlManager.RECEIVED_NEWEST_MESSAGES_URL+"0";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Auth-Token", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        response = restTemplate.exchange(url, HttpMethod.GET, entity, MessageResponse[].class);

        if (response.getStatusCode().value() == 200) {
            return response.getBody();
        } else if (response.getStatusCode().value() == 401) {
            return NO_MESSAGES;
        } else return NO_MESSAGES;
    }

    private MessageResponse[] getSentMessages() {
        final String url = UrlManager.SENT_NEWEST_MESSAGES_URL+"0";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Auth-Token", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        response = restTemplate.exchange(url, HttpMethod.GET, entity, MessageResponse[].class);

        if (response.getStatusCode().value() == 200) {
            return response.getBody();
        } else if (response.getStatusCode().value() == 401) {
            return NO_MESSAGES;
        } else return NO_MESSAGES;
    }


    public class FetchDataException extends Exception {

        public FetchDataException(String message) {
            super(message);
        }

        public FetchDataException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private void saveReceivedMessagesToSQLITE(MessageResponse[] messages) {
        for (MessageResponse message : messages) {
            ContentValues values = new ContentValues();
            values.put(ReceivedMessagesContract.ReceivedMessagesEntry._ID, message.getId());
            values.put(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_SENDER_ID, message.getSenderId());
            values.put(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_TITLE, message.getTitle());
            values.put(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_RECIPIENT_ID, message.getRecipientId());
            values.put(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_DATE_OF_SEND, message.getDateOfSend());
            values.put(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_MESSAGE_CONTENT, message.getMessageContent());
            values.put(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_WAS_READ, message.getWasRead());
            values.put(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_RECIPIENT_USERNAME, message.getRecipientUsername());
            values.put(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_SENDER_USERNAME, message.getSenderUsername());
            values.put(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_RECIPIENT_FULL_NAME, message.getRecipientFullName());
            values.put(ReceivedMessagesContract.ReceivedMessagesEntry.COLUMN_SENDER_FULL_NAME, message.getSenderFullName());
            handler.startInsert(1, null, ReceivedMessagesContract.ReceivedMessagesEntry.RECEIVED_MESSAGES_URI, values);
        }
    }

    private void saveSentMessagesToSQLITE(MessageResponse[] messages) {
        for (MessageResponse message : messages) {
            ContentValues values = new ContentValues();
            values.put(SentMessagesContract.SentMessagesEntry._ID, message.getId());
            values.put(SentMessagesContract.SentMessagesEntry.COLUMN_SENDER_ID, message.getSenderId());
            values.put(SentMessagesContract.SentMessagesEntry.COLUMN_TITLE, message.getTitle());
            values.put(SentMessagesContract.SentMessagesEntry.COLUMN_RECIPIENT_ID, message.getRecipientId());
            values.put(SentMessagesContract.SentMessagesEntry.COLUMN_DATE_OF_SEND, message.getDateOfSend());
            values.put(SentMessagesContract.SentMessagesEntry.COLUMN_MESSAGE_CONTENT, message.getMessageContent());
            values.put(SentMessagesContract.SentMessagesEntry.COLUMN_WAS_READ, message.getWasRead());
            values.put(SentMessagesContract.SentMessagesEntry.COLUMN_RECIPIENT_USERNAME, message.getRecipientUsername());
            values.put(SentMessagesContract.SentMessagesEntry.COLUMN_SENDER_USERNAME, message.getSenderUsername());
            values.put(SentMessagesContract.SentMessagesEntry.COLUMN_RECIPIENT_FULL_NAME, message.getRecipientFullName());
            values.put(SentMessagesContract.SentMessagesEntry.COLUMN_SENDER_FULL_NAME, message.getSenderFullName());
            handler.startInsert(1, null, SentMessagesContract.SentMessagesEntry.SENT_MESSAGES_URI, values);
        }
    }
}
