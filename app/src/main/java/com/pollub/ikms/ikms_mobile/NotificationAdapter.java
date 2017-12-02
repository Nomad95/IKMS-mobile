package com.pollub.ikms.ikms_mobile;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pollub.ikms.ikms_mobile.model.NotificationItemModel;

import java.util.ArrayList;

/**
 * Created by ATyKondziu on 04.11.2017.
 */

public class NotificationAdapter extends ArrayAdapter<NotificationItemModel> {

    private Activity activity;
    private ArrayList<NotificationItemModel> lNotifications;
    private static LayoutInflater inflater = null;
    private boolean isReaded = false;

    public NotificationAdapter(Activity activity, int textViewResourceId, ArrayList<NotificationItemModel> _lNotifications) {
        super(activity, textViewResourceId, _lNotifications);
        try {
            this.activity = activity;
            this.lNotifications = _lNotifications;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return lNotifications.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public NotificationItemModel getItem(int position) {return lNotifications.get(position);}

    public static class ViewHolder {
        public TextView notification_title;
        public TextView notification_content;
        public LinearLayout row;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.item, null);
                holder = new ViewHolder();

                holder.notification_title = (TextView) vi.findViewById(R.id.notification_title);
                holder.notification_content = (TextView) vi.findViewById(R.id.notification_content);
                holder.row = (LinearLayout) vi.findViewById(R.id.row);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            if(lNotifications.get(position).isRead()){
                holder.row.setBackgroundResource(R.drawable.read_notification);
            }
            else
                holder.row.setBackgroundResource(R.drawable.unread_notification);

            holder.notification_title.setText(lNotifications.get(position).getSenderFullName());
            holder.notification_content.setText(lNotifications.get(position).getContent());


        } catch (Exception e) {


        }
        return vi;
    }
}

