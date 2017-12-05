package com.pollub.ikms.ikms_mobile;

import android.app.LoaderManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pollub.ikms.ikms_mobile.model.NotificationItemModel;
import com.pollub.ikms.ikms_mobile.response.NotificationResponse;
import com.pollub.ikms.ikms_mobile.response.NotificationGroupedBySender;
import com.pollub.ikms.ikms_mobile.utils.UrlManager;
import com.pollub.ikms.ikms_mobile.utils.VocabularyCoordinators;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import lombok.SneakyThrows;

/**
 * Created by ATyKondziu on 03.11.2017.
 */
public class MyNotificationsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String token;

    private ListView lv;

    private NotificationAdapter adapter;

    private ArrayList<NotificationItemModel> listOfNotificationsForListView = new ArrayList<>();

    private NotificationGroupedBySender[] notifications;

    private NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

    private ProgressDialog dialog;
    final int TWO_SECONDS = 2 * 1000;

    private ProgressDialog progressDialog;

    private SharedPreferences prefs;
    private String tokenKey = "com.pollub.ikms.ikms_mobile.token";

    @SneakyThrows
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notifications_list);
        progressDialog = new ProgressDialog(MyNotificationsListActivity.this);
        progressDialog.setMessage("Proszę czekać..");
        progressDialog.setTitle("Logowanie");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        prefs = this.getSharedPreferences(
                "com.pollub.ikms.ikms_mobile", Context.MODE_PRIVATE);
        token = prefs.getString(tokenKey, "");
        if (token.length() > 10) {

            MyNotificationsListActivity.FetchNotificationsTask fetchNotificationsTask = new FetchNotificationsTask();

            notifications = fetchNotificationsTask.execute().get().getBody();
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
                                        + VocabularyCoordinators.changeTheNotificationWords(sizeOfNotificationsForCurrentAuthor));
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

                lv = (ListView) findViewById(R.id.lvNotifications);
                adapter = new NotificationAdapter(this, 0, listOfNotificationsForListView);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                        NotificationItemModel item = listOfNotificationsForListView.get(index);
                        if (!item.isRead()) {

                            ReadNotificationTask readNotificationTask = new ReadNotificationTask(
                                    item.getId().intValue()
                            );

                            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                            dialog = new ProgressDialog(MyNotificationsListActivity.this);
                            dialog.setMessage("Przetwarzanie...");
                            dialog.setCancelable(false);
                            dialog.show();
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    dialog.dismiss();
                                }
                            }, TWO_SECONDS);

                            readNotificationTask.execute();

                            mNotificationManager.cancel(item.getId().intValue());


                            Intent intent = new Intent(MyNotificationsListActivity.this, MyNotificationsListActivity.class);

                            //based on item add info to intent
                            startActivity(intent);
                        }
                    }


                });


            }

        }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    class FetchNotificationsTask extends AsyncTask<Void, Void, ResponseEntity<NotificationGroupedBySender[]>> {
            @Override
            protected ResponseEntity<NotificationGroupedBySender[]> doInBackground(Void... params) {
                try {
                    //Pobieramy wszystkie powiadomienia dla danego użytkownika
                    String url = UrlManager.getInstance().MY_NOTIFICATIONS_URL;
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.set("Auth-Token", token);

                    HttpEntity<String> entity = new HttpEntity<>(headers);
                    ResponseEntity<NotificationGroupedBySender[]> response = restTemplate
                            .exchange(url, HttpMethod.GET, entity, NotificationGroupedBySender[].class);
                    return response;

                } catch (Exception e) {
                    String message = e.getMessage();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ResponseEntity<NotificationGroupedBySender[]> result) {
                super.onPostExecute(result);
                HttpStatus status = result.getStatusCode();
                NotificationGroupedBySender[] notification = result.getBody();
                progressDialog.dismiss();
            }
        }

        class ReadNotificationTask extends AsyncTask<Void, Void, Void> {

            Integer index;

            public ReadNotificationTask(Integer index) {
                this.index = index;
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    String url = UrlManager.getInstance().READ_NOTIFICATION_URL + index;
                    RestTemplate restTemplate = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.set("Auth-Token", token);

                    HttpEntity<String> entity = new HttpEntity<>(headers);
                    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
                    response.getBody();
                } catch (Exception e) {
                    String message = e.getMessage();
                }
                return null;
            }
        }

        @Override
        public boolean onKeyDown ( int keyCode, KeyEvent event){
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                redirectToMainMenuActivity();
                return true;
            }

            return super.onKeyDown(keyCode, event);
        }

    public void redirectToMainMenuActivity() {
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
                | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        prefs.edit().putString(tokenKey, token).apply();
        startActivityForResult(intent, 1);
        finish();
    }
}
