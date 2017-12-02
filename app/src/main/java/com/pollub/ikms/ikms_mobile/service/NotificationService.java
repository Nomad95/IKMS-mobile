package com.pollub.ikms.ikms_mobile.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.pollub.ikms.ikms_mobile.dto.NotificationGroupedBySenderDTO;
import com.pollub.ikms.ikms_mobile.response.NotificationGroupedBySender;
import com.pollub.ikms.ikms_mobile.utils.UrlManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.SneakyThrows;

/**
 * Created by ATyKondziu on 18.11.2017.
 */

public class NotificationService extends IntentService {
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "NotificationService";

    private String token;

    private SharedPreferences prefs;
    private String tokenKey = "com.pollub.ikms.ikms_mobile.token";

    private List<NotificationGroupedBySenderDTO> allNotificationsDTO;

    ResponseEntity<NotificationGroupedBySender[]> response;

    public NotificationService() {
        super(TAG);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service Started!");
        prefs = this.getSharedPreferences(
                "com.pollub.ikms.ikms_mobile", Context.MODE_PRIVATE);
        token = prefs.getString(tokenKey, "");
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Bundle notificationsBundle = new Bundle();
        receiver.send(STATUS_RUNNING, notificationsBundle);
        try {
            allNotificationsDTO = mapNotificationGroupedBySenderToDTOArrayList(getNotifications());
            if (allNotificationsDTO != null) {
                notificationsBundle.putInt("unreadNotifications", countAllUnreadMessage());
                //notificationsBundle.putParcelableArrayList("result", new ArrayList<Parcelable>(allNotificationsDTO));
                receiver.send(STATUS_FINISHED, notificationsBundle);
            }
            else{
                receiver.send(STATUS_FINISHED, notificationsBundle);
            }
        } catch (Exception e) {
            notificationsBundle.putString(Intent.EXTRA_TEXT, e.getMessage());
            receiver.send(STATUS_ERROR, notificationsBundle);
        }

        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }

    private int countAllUnreadMessage() {
        int counter = 0;
        for (NotificationGroupedBySenderDTO notificationGroupedBySenderDTO : allNotificationsDTO) {
            counter+=notificationGroupedBySenderDTO.getNumberOfUnread();
        }
        return counter;
    }

    @SneakyThrows
    private NotificationGroupedBySender[] getNotifications() {
        final String url = UrlManager.getInstance().MY_NOTIFICATIONS_URL;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Auth-Token", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        response = restTemplate.exchange(url, HttpMethod.GET, entity, NotificationGroupedBySender[].class);

        if (response.getStatusCode().value() == 200) {
            return response.getBody();
        } else if (response.getStatusCode().value() == 401) {
            return null;
        } else {
            throw new NotificationService.FetchDataException("Nie udało się pobrać powiadomień");
        }
    }



    public class FetchDataException extends Exception {

        public FetchDataException(String message) {
            super(message);
        }

        public FetchDataException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public List<NotificationGroupedBySenderDTO> mapNotificationGroupedBySenderToDTOArrayList(NotificationGroupedBySender[] notifications) {
        List<NotificationGroupedBySenderDTO> notificationsDTO = new ArrayList<>();
        for (NotificationGroupedBySender notificationGroupedBySender : notifications) {
            notificationsDTO.add(new NotificationGroupedBySenderDTO(
                    notificationGroupedBySender.getSenderFullName(),
                    notificationGroupedBySender.getNotifications(),
                    notificationGroupedBySender.getNumberOfUnread()));

        }
        return notificationsDTO;
    }
}
