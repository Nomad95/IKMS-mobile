package com.pollub.ikms.ikms_mobile.utils;

import com.pollub.ikms.ikms_mobile.utils.constants.Urls;

/**
 * Created by Konrad Depta on 03.11.2017.
 */
public final class UrlManager {
    private static final KindOfServer SERVER = KindOfServer.NGROK;
    public static String AUTH_LOGIN_URL;
    public static String EMPLOYEE_URL;
    public static String MY_NOTIFICATIONS_URL;
    public static String READ_NOTIFICATION_URL;
    public static String PHONE_NUMBERS_URL;
    public static String SENDING_NEW_MESSAGE;

    private static final UrlManager instance = new UrlManager();

    private UrlManager() {
        setUrls();
    }

    public static UrlManager getInstance(){
        return instance;
    }

    private static void setUrls() {
        switch (SERVER) {
            case HEROKU:
                AUTH_LOGIN_URL = Urls.AUTH_LOGIN_HEROKU;
                EMPLOYEE_URL = Urls.EMPLOYEE_HEROKU;
                MY_NOTIFICATIONS_URL = Urls.MY_NOTIFICATIONS_HEROKU;
                READ_NOTIFICATION_URL = Urls.READ_NOTIFICATION_HEROKU;
                PHONE_NUMBERS_URL = Urls.PHONE_NUMBERS_HEROKU;
                SENDING_NEW_MESSAGE = Urls.SENDING_NEW_MESSAGE_HEROKU;
                break;
            case NGROK:
                AUTH_LOGIN_URL = Urls.AUTH_LOGIN_NGROK;
                EMPLOYEE_URL = Urls.EMPLOYEE_NGROK;
                MY_NOTIFICATIONS_URL = Urls.MY_NOTIFICATIONS_NGROK;
                READ_NOTIFICATION_URL = Urls.READ_NOTIFICATION_NGROK;
                PHONE_NUMBERS_URL = Urls.PHONE_NUMBERS_NGROK;
                SENDING_NEW_MESSAGE = Urls.SENDING_NEW_MESSAGE_NGROK;
                break;
            default:
                AUTH_LOGIN_URL = Urls.AUTH_LOGIN_HEROKU;
                EMPLOYEE_URL = Urls.AUTH_LOGIN_HEROKU;
                MY_NOTIFICATIONS_URL = Urls.MY_NOTIFICATIONS_HEROKU;
                READ_NOTIFICATION_URL = Urls.READ_NOTIFICATION_HEROKU;
                SENDING_NEW_MESSAGE = Urls.SENDING_NEW_MESSAGE_HEROKU;
        }
    }
}
