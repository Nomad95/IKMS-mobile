package com.pollub.ikms.ikms_mobile.utils.constants;

/**
 * Created by Konrad Depta on 03.11.2017.
 */
public final class Urls {

        public static final String HEROKU_DOMAIN = "https://ikmsdeploy.herokuapp.com//";
        public static final String NGROK_DOMAIN = "https://2aca9c7c.ngrok.io//";

        public static final String AUTH_LOGIN_HEROKU = HEROKU_DOMAIN + "auth/login";
        public static final String AUTH_LOGIN_NGROK = NGROK_DOMAIN + "auth/login";

        public static final String EMPLOYEE_HEROKU = HEROKU_DOMAIN + "api/employee/1";
        public static final String EMPLOYEE_NGROK = NGROK_DOMAIN + "api/employee/1";

        public static final String MY_NOTIFICATIONS_HEROKU = HEROKU_DOMAIN + "api/notification/myAllNotifications";
        public static final String MY_NOTIFICATIONS_NGROK = NGROK_DOMAIN + "api/notification/myAllNotifications";

        public static final String READ_NOTIFICATION_HEROKU = HEROKU_DOMAIN + "api/notification/myNotifications/read/";
        public static final String READ_NOTIFICATION_NGROK = NGROK_DOMAIN + "api/notification/myNotifications/read/";

        public static final String PHONE_NUMBERS_HEROKU = HEROKU_DOMAIN + "api/employee/phoneNumbers";
        public static final String PHONE_NUMBERS_NGROK = NGROK_DOMAIN + "api/employee/phoneNumbers";
}
