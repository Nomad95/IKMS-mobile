package com.pollub.ikms.ikms_mobile.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

/**
 * Created by ATyKondziu on 29.11.2017.
 */

public class MessagesQueryHandler extends AsyncQueryHandler {
    public MessagesQueryHandler(ContentResolver cr) {
        super(cr);
    }
}
