package com.pollub.ikms.ikms_mobile.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.pollub.ikms.ikms_mobile.MyNotificationsListActivity;
import com.pollub.ikms.ikms_mobile.R;

/**
 * Created by ATyKondziu on 19.11.2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    private SharedPreferences prefs;

    private String tokenKey = "com.pollub.ikms.ikms_mobile.token";

    private String token;

    @Override
    public void onReceive(Context context, Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();

        boolean isConnected = wifi != null &&
                wifi.isConnected();

        prefs = context.getSharedPreferences(
                "com.pollub.ikms.ikms_mobile", Context.MODE_PRIVATE);

        if (isConnected) {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            builder.setColor(Color.BLACK)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_icon_large))
                    .setContentTitle("Token");



            token = prefs.getString(tokenKey, "");
            Log.d("Netowk Available ", "Flag No 1");

            Intent notificationIntent = new Intent(context, MyNotificationsListActivity.class);
            notificationIntent.putExtra("token", token);
            builder.setContentText(token);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);
            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            mNotificationManager.notify(1, builder.build());
        }

    }

}
