package com.pollub.ikms.ikms_mobile;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ListView;

import com.pollub.ikms.ikms_mobile.data.NotificationsContract;
import com.pollub.ikms.ikms_mobile.response.NotificationGroupedBySender;
import com.pollub.ikms.ikms_mobile.utils.UrlManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import lombok.SneakyThrows;

/**
 * Created by ATyKondziu on 03.11.2017.
 */
public class MyNotificationsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String token;

    private ListView lv;

    private NotificationsCursorAdapter notificationsCursorAdapter;

    private Cursor cursor;

    private static final int URL_LOADER = 0;

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

        prefs = this.getSharedPreferences(
                "com.pollub.ikms.ikms_mobile", Context.MODE_PRIVATE);
        token = prefs.getString(tokenKey, "");

        getLoaderManager().initLoader(URL_LOADER, null, this);
        //ListView - set adapter
        lv = (ListView) findViewById(R.id.lvNotifications);

        notificationsCursorAdapter = new NotificationsCursorAdapter(this, cursor, false);

        lv.setAdapter(notificationsCursorAdapter);

        lv.setOnItemClickListener((adapterView, view, position, id) -> {
            cursor = (Cursor) adapterView.getItemAtPosition(position);
            Long notificationId = cursor.getLong(cursor.getColumnIndex(NotificationsContract.NotificationsEntry._ID));

            int isRead= cursor.getInt(cursor.getColumnIndex(NotificationsContract.NotificationsEntry.COLUMN_WAS_READ));

            if (isRead==0) {

                ReadNotificationTask readNotificationTask = new ReadNotificationTask(
                        notificationId.intValue()
                );
                dialog = new ProgressDialog(MyNotificationsListActivity.this);
                dialog.setMessage("Przetwarzanie...");
                dialog.setCancelable(false);
                dialog.show();
                new Handler().postDelayed(() -> dialog.dismiss(), TWO_SECONDS);
                String[] args = {String.valueOf(notificationId)};
                ContentValues values = new ContentValues();
                values.put(NotificationsContract.NotificationsEntry.COLUMN_WAS_READ,1);
                getContentResolver().update(NotificationsContract.NotificationsEntry.NOTIFICATIONS_URI,values, NotificationsContract.NotificationsEntry._ID + " =?", args);

                readNotificationTask.execute();

                Intent intent = new Intent(MyNotificationsListActivity.this, MyNotificationsListActivity.class);
                startActivity(intent);
            }

        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {NotificationsContract.NotificationsEntry.COLUMN_CONTENT,
                NotificationsContract.NotificationsEntry.TABLE_NAME + "." + NotificationsContract.NotificationsEntry._ID,
                NotificationsContract.NotificationsEntry.COLUMN_WAS_READ};
     return  new CursorLoader(
                this,
                NotificationsContract.NotificationsEntry.NOTIFICATIONS_URI,
                projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        notificationsCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        notificationsCursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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

    class ReadNotificationTask extends AsyncTask<Void, Void, Void> {

        Integer index;

        public ReadNotificationTask(Integer index) {
            this.index = index;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String url = UrlManager.READ_NOTIFICATION_URL + index;
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
}
