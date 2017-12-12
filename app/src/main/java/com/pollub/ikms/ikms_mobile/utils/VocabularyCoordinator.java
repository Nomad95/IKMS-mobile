package com.pollub.ikms.ikms_mobile.utils;

/**
 * Created by ATyKondziu on 12.11.2017.
 */

public class VocabularyCoordinator {
        public static String changeTheNotificationWords(int numberOfNotifications) {
            if (numberOfNotifications == 2 || numberOfNotifications == 3 || numberOfNotifications == 4)
                return " nowe powiadomienia";
            else return " nowych powiadomień";
        }
}
