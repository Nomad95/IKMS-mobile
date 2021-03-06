package com.pollub.ikms.ikms_mobile.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.pollub.ikms.ikms_mobile.MyNotificationsListActivity;
import com.pollub.ikms.ikms_mobile.R;
import com.pollub.ikms.ikms_mobile.data.NotificationsContract;
import com.pollub.ikms.ikms_mobile.data.NotificationsQueryHandler;
import com.pollub.ikms.ikms_mobile.data.SendersContract;
import com.pollub.ikms.ikms_mobile.dto.NotificationGroupedBySenderDto;
import com.pollub.ikms.ikms_mobile.model.NotificationItemModel;
import com.pollub.ikms.ikms_mobile.response.NotificationGroupedBySender;
import com.pollub.ikms.ikms_mobile.response.NotificationResponse;
import com.pollub.ikms.ikms_mobile.utils.UrlManager;
import com.pollub.ikms.ikms_mobile.utils.VocabularyCoordinator;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static com.pollub.ikms.ikms_mobile.utils.constants.StatusCode.STATUS_ERROR;
import static com.pollub.ikms.ikms_mobile.utils.constants.StatusCode.STATUS_FINISHED;
import static com.pollub.ikms.ikms_mobile.utils.constants.StatusCode.STATUS_RUNNING;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ATyKondziu on 18.11.2017.
 */

public class FetchNotificationsIntentService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "NotificationService";

    private String token;

    private SharedPreferences prefs;
    private String tokenKey = "com.pollub.ikms.ikms_mobile.token";

    private List<NotificationGroupedBySenderDto> allNotificationsDTO;

    private ResponseEntity<NotificationGroupedBySender[]> response;

    private static final NotificationGroupedBySender[] NO_NOTIFICATIONS = {};

    private NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

    private NotificationsQueryHandler handler;

    private ArrayList<NotificationItemModel> listOfNotificationsForListView = new ArrayList<>();

    public FetchNotificationsIntentService() {
        super(TAG);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "FetchNotificationsIntentService Started!");
        prefs = this.getSharedPreferences(
                "com.pollub.ikms.ikms_mobile", Context.MODE_PRIVATE);
        token = prefs.getString(tokenKey, "");
        handler = new NotificationsQueryHandler(getContentResolver());

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


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

        Log.d(TAG, "FetchNotificationsIntentService Stopping!");
       // this.stopSelf();
    }

    private int countAllUnreadMessage() {
        int counter = 0;
        for (NotificationGroupedBySenderDto notificationGroupedBySenderDto : allNotificationsDTO) {
            counter+= notificationGroupedBySenderDto.getNumberOfUnread();
        }
        return counter;
    }


    private NotificationGroupedBySender[] getNotifications() {
        final String url = UrlManager.MY_NOTIFICATIONS_URL;
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
            return NO_NOTIFICATIONS;
        }
        else return NO_NOTIFICATIONS;
    }



    public class FetchDataException extends Exception {

        public FetchDataException(String message) {
            super(message);
        }

        public FetchDataException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private List<NotificationGroupedBySenderDto> mapNotificationGroupedBySenderToDTOArrayList(NotificationGroupedBySender[] notifications) {
        List<NotificationGroupedBySenderDto> notificationsDTO = new ArrayList<>();
        for (NotificationGroupedBySender notificationGroupedBySender : notifications) {
            notificationsDTO.add(new NotificationGroupedBySenderDto(
                    notificationGroupedBySender.getSenderFullName(),
                    notificationGroupedBySender.getNotifications(),
                    notificationGroupedBySender.getNumberOfUnread()));


            ContentValues values = new ContentValues();
            values.put(SendersContract.SendersEntry._ID, notificationGroupedBySender.getSenderId());
            values.put(SendersContract.SendersEntry.COLUMN_SENDER_FULL_NAME, notificationGroupedBySender.getSenderFullName());
            handler.startInsert(1,null, SendersContract.SendersEntry.SENDERS_URI, values);
            for(NotificationResponse notification : notificationGroupedBySender.getNotifications()){
                ContentValues values2 = new ContentValues();
                values2.put(NotificationsContract.NotificationsEntry._ID,notification.getId());
                values2.put(NotificationsContract.NotificationsEntry.COLUMN_CONTENT,notification.getContent());
                values2.put(NotificationsContract.NotificationsEntry.COLUMN_DATE_OF_SEND,notification.getDateOfSend());
                values2.put(NotificationsContract.NotificationsEntry.COLUMN_PRIORITY,notification.getPriority());
                values2.put(NotificationsContract.NotificationsEntry.COLUMN_WAS_READ,notification.getWasRead());
                values2.put(NotificationsContract.NotificationsEntry.COLUMN_SENDER_ID_COLUMN,notificationGroupedBySender.getSenderId());
                handler.startInsert(1,null, NotificationsContract.NotificationsEntry.NOTIFICATIONS_URI, values2);
            }

        }
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, MyNotificationsListActivity.class);
        notificationIntent.putExtra("token", token);
        for (int i = 0; i < notifications.length; i++) {
            if (notifications[i].getNumberOfUnread() != 0) {
                builder.setColor(Color.BLACK)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification_icon_large))
                        .setContentTitle(notifications[i].getSenderFullName());
            }

            int sizeOfNotificationsForCurrentAuthor = notifications[i].getNumberOfUnread();
            if (notifications[i].getNumberOfUnread() > 1) {
                NotificationCompat.InboxStyle inboxStyle =
                        new NotificationCompat.InboxStyle();

                //inboxStyle.setBigContentTitle("Wszystkie powiadomienia:");
                for (NotificationResponse notification : notifications[i].getNotifications()) {
                    listOfNotificationsForListView.add(new NotificationItemModel(
                            notification.getId(),
                            notifications[i].getSenderFullName(),
                            notification.getContent(),
                            notification.getWasRead()));

                    if (!notification.getWasRead())
                        inboxStyle.addLine(notification.getContent());

                    builder.setContentText(
                            sizeOfNotificationsForCurrentAuthor
                                    + VocabularyCoordinator.changeTheNotificationWords(sizeOfNotificationsForCurrentAuthor));
                }

                builder.setStyle(inboxStyle);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());

                stackBuilder.addNextIntent(notificationIntent);

                PendingIntent contentIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentIntent(contentIntent);
                builder.mNumber = sizeOfNotificationsForCurrentAuthor;
            } else if (notifications[i].getNumberOfUnread() == 1) {

                listOfNotificationsForListView.add(new NotificationItemModel(
                        notifications[i].getNotifications().get(0).getId(),
                        notifications[i].getSenderFullName(),
                        notifications[i].getNotifications().get(0).getContent(),
                        notifications[i].getNotifications().get(0).getWasRead()));

                builder.mNumber = 0;
                if (!notifications[i].getNotifications().get(0).getWasRead())
                    builder.setContentText(notifications[i].getNotifications().get(0).getContent());

                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                        | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                        | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(contentIntent);
            } else {
                for (NotificationResponse notification : notifications[i].getNotifications()) {
                    listOfNotificationsForListView.add(new NotificationItemModel(
                            notification.getId(),
                            notifications[i].getSenderFullName(),
                            notification.getContent(),
                            notification.getWasRead()));

                }
            }
            if (notifications[i].getNumberOfUnread() != 0) {
                builder.setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                mNotificationManager.notify(i, builder.build());


            }
        }
        return notificationsDTO;
    }
}
