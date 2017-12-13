package com.pollub.ikms.ikms_mobile;

import android.content.AsyncTaskLoader;
import android.util.Log;

import com.pollub.ikms.ikms_mobile.data.DBHelper;
import com.pollub.ikms.ikms_mobile.model.NotificationItemModel;

import java.util.ArrayList;

/**
 * Created by ATyKondziu on 10.12.2017.
 */

public class NotificationsAsyncTaskLoader extends AsyncTaskLoader<ArrayList<NotificationItemModel>> {

        private ArrayList<NotificationItemModel> notifications;
        private final static String TAG = NotificationsAsyncTaskLoader.class.getSimpleName();
        DBHelper db ;

        public NotificationsAsyncTaskLoader(MyNotificationsListActivity activity, ArrayList<NotificationItemModel> notifications) {
            super(activity);
            this.notifications = notifications;
            Log.i(TAG, "init Asynctask Loader");
        }

        @Override
        public ArrayList<NotificationItemModel> loadInBackground() {
            //loading data here
            try {
                synchronized (this) {

                    Log.i(TAG, "load in background");
                }
            } catch (Exception e) {
                e.getMessage();
            }
            return notifications;
        }

       /* @Override
        public void deliverResult(ArrayList<Friend> data) {
            super.deliverResult(data);
            Log.i(TAG, "deliver Result");
        }

        public ArrayList<NotificationItemModel> getNotifications() {
           db.getReadableDatabase().;
        }*/

}
