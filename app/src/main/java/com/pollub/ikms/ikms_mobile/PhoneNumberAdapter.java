package com.pollub.ikms.ikms_mobile;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pollub.ikms.ikms_mobile.model.NotificationItemModel;
import com.pollub.ikms.ikms_mobile.model.PhoneNumberItemModel;

import java.util.ArrayList;

public class PhoneNumberAdapter extends ArrayAdapter<PhoneNumberItemModel> {

    private Activity activity;
    private ArrayList<PhoneNumberItemModel> phoneNumbers;
    private static LayoutInflater inflater = null;

    public PhoneNumberAdapter(Activity activity, int textViewResourceId, ArrayList<PhoneNumberItemModel> _phoneNumbers) {
        super(activity, textViewResourceId, _phoneNumbers);
        try {
            this.activity = activity;
            this.phoneNumbers = _phoneNumbers;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return phoneNumbers.size();
    }

    public long getItemId(int position) {
        return position;
    }

    public PhoneNumberItemModel getItem(int position) {return phoneNumbers.get(position);}

    public static class ViewHolder {
        public TextView fullName;
        public TextView number;
        public LinearLayout row;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.phone_number_row, null);
                holder = new ViewHolder();

                holder.fullName = (TextView) vi.findViewById(R.id.employee_full_name);
                holder.number = (TextView) vi.findViewById(R.id.phone_number);
                holder.row = (LinearLayout) vi.findViewById(R.id.phone_number_row);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            holder.fullName.setText(phoneNumbers.get(position).getFullName());
            holder.number.setText(phoneNumbers.get(position).getNumber());
        } catch (Exception e) {
            Log.e("ERROR", "Error while inflating phoneNumberListView");
        }

        return vi;
    }
}

