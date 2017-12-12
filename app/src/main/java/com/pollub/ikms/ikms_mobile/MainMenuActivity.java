package com.pollub.ikms.ikms_mobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pollub.ikms.ikms_mobile.messagebox.MyMessagesListActivity;
import com.pollub.ikms.ikms_mobile.receiver.RequestResultReceiver;
import com.pollub.ikms.ikms_mobile.service.FetchMessagesIntentService;
import com.pollub.ikms.ikms_mobile.service.FetchNotificationsIntentService;
import com.pollub.ikms.ikms_mobile.utils.UrlManager;

public class MainMenuActivity extends AppCompatActivity implements RequestResultReceiver.Receiver {

    private String token;

    private LinearLayout goToNotifications;
    private LinearLayout goToPhoneNumbers;
    private LinearLayout goToMessages;

    private SharedPreferences prefs;
    private final String tokenKey = "com.pollub.ikms.ikms_mobile.token";
    private String unreadMessagesKey = "com.pollub.ikms.ikms_mobile.unread_messages";
    private String unreadNotificationsKey = "com.pollub.ikms.ikms_mobile.unread_notifications";

    private RequestResultReceiver notificationsReceiver;

    private RequestResultReceiver messagesReceiver;


    private ProgressDialog progressDialog;

    private int allUnreadNotifications;

    private int allUnreadMessages;

    private TextView tvUnreadNotificationsQuantity;

    private TextView tvUnreadMessagesQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        tvUnreadNotificationsQuantity = (TextView) findViewById(R.id.unread_notifications_counter);
        tvUnreadMessagesQuantity = (TextView) findViewById(R.id.unread_messages_counter);



        prefs = this.getSharedPreferences(
                "com.pollub.ikms.ikms_mobile", Context.MODE_PRIVATE);
        token = prefs.getString(tokenKey, "");
        tvUnreadNotificationsQuantity.setText(prefs.getString(unreadNotificationsKey, ""));
        tvUnreadMessagesQuantity.setText(prefs.getString(unreadMessagesKey, ""));

        if (token.length() > 10) {
            notificationsReceiver = new RequestResultReceiver(new Handler());
            notificationsReceiver.setReceiver(MainMenuActivity.this);
            Intent fetchNotificationsIntent = new Intent(Intent.ACTION_SYNC, null,
                    MainMenuActivity.this, FetchNotificationsIntentService.class);

            fetchNotificationsIntent.putExtra("receiver", notificationsReceiver);

            messagesReceiver = new RequestResultReceiver(new Handler());
            messagesReceiver.setReceiver(MainMenuActivity.this);
            Intent fetchMessagesIntent = new Intent(Intent.ACTION_SYNC, null,
                    MainMenuActivity.this, FetchMessagesIntentService.class);

            fetchMessagesIntent.putExtra("receiver", messagesReceiver);

            startService(fetchNotificationsIntent);
            startService(fetchMessagesIntent);

            goToNotifications = (LinearLayout) findViewById(R.id.go_to_notifications);
            goToNotifications.setOnClickListener(view -> redirectToNotifications());

            goToPhoneNumbers = (LinearLayout) findViewById(R.id.go_to_call);
            goToPhoneNumbers.setOnClickListener(view -> redirectToPhoneNumbers());

            goToMessages = (LinearLayout) findViewById(R.id.go_to_messages);
            goToMessages.setOnClickListener(view -> redirectToMessages());
        } else {
            redirectToLoginActivity();
        }

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Naciśnij jeszcze raz aby wyjść z IKMS", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case FetchNotificationsIntentService.STATUS_RUNNING:
                break;

            case FetchNotificationsIntentService.STATUS_FINISHED:
                allUnreadNotifications = resultData.getInt("unreadNotifications", 0);
                prefs.edit().putString(unreadNotificationsKey, Integer.toString(allUnreadNotifications)).apply();
                if (allUnreadNotifications > 0) {
                    tvUnreadNotificationsQuantity.setText(prefs.getString(unreadNotificationsKey, ""));
                    tvUnreadNotificationsQuantity.setVisibility(View.VISIBLE);
                } else
                    tvUnreadNotificationsQuantity.setVisibility(View.INVISIBLE);
                break;

            case FetchNotificationsIntentService.STATUS_ERROR:
                String statusCode = resultData.getString(Intent.EXTRA_TEXT);
                tvUnreadNotificationsQuantity.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Połączenie z serwerem nieudane", Toast.LENGTH_SHORT).show();
                break;

            case FetchMessagesIntentService.STATUS_RUNNING:
                break;

            case FetchMessagesIntentService.STATUS_FINISHED:
                allUnreadMessages = resultData.getInt("unreadMessages", 0);
                prefs.edit().putString(unreadMessagesKey, Integer.toString(allUnreadMessages)).apply();
                if (allUnreadMessages > 0) {
                    tvUnreadMessagesQuantity.setText(prefs.getString(unreadMessagesKey, ""));
                    tvUnreadMessagesQuantity.setVisibility(View.VISIBLE);
                } else
                    tvUnreadMessagesQuantity.setVisibility(View.INVISIBLE);
                break;

            case FetchMessagesIntentService.STATUS_ERROR:
                String fetchMessagesStatusCode = resultData.getString(Intent.EXTRA_TEXT);
                tvUnreadMessagesQuantity.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Połączenie z serwerem nieudane", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void redirectToNotifications() {
        Intent intent = new Intent(this, MyNotificationsListActivity.class);
        startActivityForResult(intent, 1);
    }

    public void redirectToPhoneNumbers() {
        Intent intent = new Intent(this, PhoneCallActivity.class);
        startActivityForResult(intent, 1);
    }

    private void redirectToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    public void redirectToMessages() {
        Intent intent = new Intent(this, MyMessagesListActivity.class);
        startActivityForResult(intent, 1);
    }
}
