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
import com.pollub.ikms.ikms_mobile.service.LoginService;
import com.pollub.ikms.ikms_mobile.service.FetchNotificationsIntentService;
import com.pollub.ikms.ikms_mobile.utils.UrlManager;

public class MainMenuActivity extends AppCompatActivity  implements RequestResultReceiver.Receiver{

    private String token;

    private LinearLayout goToNotifications;

    private LinearLayout goToMessages;

    private SharedPreferences prefs;
    private final String tokenKey = "com.pollub.ikms.ikms_mobile.token";

    private RequestResultReceiver receiver;

    private ProgressDialog progressDialog;

    private int allUnreadNotifications;

    private TextView tvUnreadNotificationsQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        tvUnreadNotificationsQuantity = (TextView) findViewById(R.id.unread_notifications_counter);
        prefs = this.getSharedPreferences(
                "com.pollub.ikms.ikms_mobile", Context.MODE_PRIVATE);
        token = prefs.getString(tokenKey, "");
        if (token.length() > 10) {

               /* Starting Login Service */
            receiver = new RequestResultReceiver(new Handler());
            receiver.setReceiver(MainMenuActivity.this);
            Intent fetchNotificationsIntent = new Intent(Intent.ACTION_SYNC, null,
                    MainMenuActivity.this, FetchNotificationsIntentService.class);

                /* Send optional extras to Login IntentService */
            fetchNotificationsIntent.putExtra("url", UrlManager.getInstance().MY_NOTIFICATIONS_URL);
            fetchNotificationsIntent.putExtra("receiver", receiver);
            fetchNotificationsIntent.putExtra("requestId", 102);

            startService(fetchNotificationsIntent);

            Intent fetchMessagesIntent = new Intent(Intent.ACTION_SYNC, null,
                    MainMenuActivity.this, FetchMessagesIntentService.class);

           /*     *//* Send optional extras to Login IntentService *//*
            fetchMessagesIntent.putExtra("url", UrlManager.getInstance().MY_NOTIFICATIONS_URL);
            fetchMessagesIntent.putExtra("receiver", receiver);
            fetchMessagesIntent.putExtra("requestId", 102);

            startService(fetchNotificationsIntent);*/

            goToNotifications = (LinearLayout) findViewById(R.id.go_to_notifications);
            goToNotifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectToNotifications();
                }
            });

            goToMessages = (LinearLayout) findViewById(R.id.go_to_messages);
            goToMessages.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    redirectToMessages();
                }
            });
        }
        else{
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
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void redirectToNotifications() {
        Intent intent = new Intent(this, MyNotificationsListActivity.class);
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

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case LoginService.STATUS_RUNNING:
                progressDialog = new ProgressDialog(MainMenuActivity.this);
               // progressDialog.setTitle("ładowanie");
                progressDialog.setMessage("Ładowanie...");
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(true);
                progressDialog.show();
                break;

            case LoginService.STATUS_FINISHED:
                allUnreadNotifications = resultData.getInt("unreadNotifications", 0);
                if(allUnreadNotifications >0){
                    tvUnreadNotificationsQuantity.setText(Integer.toString(allUnreadNotifications));
                    tvUnreadNotificationsQuantity.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
                else
                    tvUnreadNotificationsQuantity.setVisibility(View.GONE);
                break;

            case LoginService.STATUS_ERROR:
                String statusCode = resultData.getString(Intent.EXTRA_TEXT);
                progressDialog.dismiss();
                Toast.makeText(this, "Niepoprawne dane logowania", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
