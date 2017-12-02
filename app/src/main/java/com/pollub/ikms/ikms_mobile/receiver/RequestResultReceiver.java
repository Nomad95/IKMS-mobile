package com.pollub.ikms.ikms_mobile.receiver;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

import lombok.Setter;

/**
 * Created by ATyKondziu on 17.11.2017.
 */
@Setter
public class RequestResultReceiver extends ResultReceiver {

    private Receiver receiver;

    @SuppressLint("RestrictedApi")
    public RequestResultReceiver(Handler handler) {
        super(handler);
    }

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver != null) {
            receiver.onReceiveResult(resultCode, resultData);
        }
    }
}
