package com.pollub.ikms.ikms_mobile.service;


import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import com.pollub.ikms.ikms_mobile.exceptions.AuthorizationException;
import com.pollub.ikms.ikms_mobile.request.NewMessageRequest;
import com.pollub.ikms.ikms_mobile.response.MessageResponse;
import com.pollub.ikms.ikms_mobile.utils.UrlManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import lombok.SneakyThrows;

public class SendingMessageService extends IntentService {

    public static final int STATUS_RUNNING = 6;
    public static final int STATUS_FINISHED = 7;
    public static final int STATUS_ERROR = 8;

    private static final String TAG = "SendingMessageService";

    private String token;

    private SharedPreferences prefs;

    private String tokenKey = "com.pollub.ikms.ikms_mobile.token";

    private ResponseEntity<MessageResponse> response;

    MessageResponse messageResponse;

    public SendingMessageService() {
        super(TAG);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "WYsylanie wiadomości");
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        NewMessageRequest newMessageRequest = new NewMessageRequest();
        newMessageRequest.setRecipientUsername(intent.getExtras().getString("username"));
        newMessageRequest.setTitle(intent.getExtras().getString("title"));
        newMessageRequest.setMessageContents(intent.getExtras().getString("content"));
        Bundle newMessageBundle = new Bundle();
        receiver.send(STATUS_RUNNING, newMessageBundle);
        messageResponse = sendMessage(newMessageRequest);
        if(messageResponse != null) {
            receiver.send(STATUS_FINISHED, newMessageBundle);
        }
        stopSelf();
    }


    @SneakyThrows
    private MessageResponse sendMessage(NewMessageRequest newMessageRequest){
        prefs = this.getSharedPreferences(
                "com.pollub.ikms.ikms_mobile", Context.MODE_PRIVATE);
        token = prefs.getString(tokenKey, "");

        final String url = String.format(UrlManager.SENDING_NEW_MESSAGE, newMessageRequest.getRecipientUsername());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Auth-Token", token);

        HttpEntity<NewMessageRequest> entity = new HttpEntity<>(newMessageRequest, headers);
        response = restTemplate.exchange(url, HttpMethod.POST, entity, MessageResponse.class );
        if (response.getStatusCode().value() == 201) {
            return response.getBody();
        } else {
            //todo [Arek] Trzeba by poczytać czy jest coś takiego podobnego do AdviceControllera który będzie wychwytywał wszystkie
            // wyjątki rzucone i zamieniał je na odpowiednie toasty

            Toast.makeText(getApplicationContext(), "Błąd podczas wysyłania wiadomości",
                    Toast.LENGTH_SHORT).show();
          throw new AuthorizationException("Nie udało się wysłać wiadomości");
        }
    }
}
