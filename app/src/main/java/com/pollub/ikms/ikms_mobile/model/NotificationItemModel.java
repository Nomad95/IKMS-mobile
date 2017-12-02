package com.pollub.ikms.ikms_mobile.model;

import lombok.Data;

/**
 * Created by ATyKondziu on 05.11.2017.
 */
@Data
public class NotificationItemModel {

    private Long id;

    private String senderFullName;

    private String content;

    private boolean isRead;

    public NotificationItemModel(Long id, String senderFullName, String content, boolean isRead) {
        this.id = id;
        this.senderFullName = senderFullName;
        this.content = content;
        this.isRead = isRead;
    }
}
