package com.pollub.ikms.ikms_mobile.messagebox;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pollub.ikms.ikms_mobile.R;
import com.pollub.ikms.ikms_mobile.model.MessageItemModel;

import java.util.ArrayList;

public class OutboxAdapter extends ArrayAdapter<MessageItemModel> {

    private Activity activity;
    private ArrayList<MessageItemModel> lMessages;
    private static LayoutInflater inflater = null;
    private boolean isReaded = false;

    public OutboxAdapter(Activity activity, int textViewResourceId, ArrayList<MessageItemModel> _lMessages) {
        super(activity, textViewResourceId, _lMessages);
        try {
            this.activity = activity;
            this.lMessages = _lMessages;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return lMessages.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public MessageItemModel getItem(int position) {return lMessages.get(position);}

    public static class ViewHolder {
        public TextView to;
        public TextView title;
        public TextView dateOfSend;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.outbox_list_item, null);
                holder = new ViewHolder();

                holder.to = (TextView) vi.findViewById(R.id.outbox_item_to);
                holder.title = (TextView) vi.findViewById(R.id.outbox_item_title);
                holder.dateOfSend = (TextView) vi.findViewById(R.id.outbox_item_date);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }


            holder.to.setText(lMessages.get(position).getRecipientFullName());
            holder.title.setText(lMessages.get(position).getTitle());
            holder.dateOfSend.setText(lMessages.get(position).getDateOfSend());

        } catch (Exception e) {


        }
        return vi;
    }


}
