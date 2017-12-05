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
import com.pollub.ikms.ikms_mobile.data.SendersContract;
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
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "MessagesService";

    private String token;

    private SharedPreferences prefs;
    private String tokenKey = "com.pollub.ikms.ikms_mobile.token";

    private MessageResponse[] allMessagesDTO;

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
            allMessagesDTO = getMessages();
            if (allMessagesDTO != null) {
                messagesBundle.putInt("unreadMessages", countAllUnreadMessage());
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

    private int countAllUnreadMessage() {
        int counter = 0;
        for (MessageResponse messageResponse : allMessagesDTO) {
            counter += messageResponse.getNumberOfUnread();
        }
        return counter;
    }


    private MessageResponse[] getMessages() {
        final String url = UrlManager.getInstance().MY_NOTIFICATIONS_URL;
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
            return null;
        } else return null;
    }


    public class FetchDataException extends Exception {

        public FetchDataException(String message) {
            super(message);
        }

        public FetchDataException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private void saveToSQLITE(MessageResponse[] messages) {
        for (MessageResponse message : messages) {
            ContentValues values = new ContentValues();
            values.put(SendersContract.SendersEntry._ID, message.getId());
            values.put(SendersContract.SendersEntry.COLUMN_SENDER_FULL_NAME, message.getSenderFullName());
            handler.startInsert(1, null, SendersContract.SendersEntry.SENDERS_URI, values);
        }
    }
}
