package com.pollub.ikms.ikms_mobile.dto;

import com.pollub.ikms.ikms_mobile.response.NotificationResponse;

import java.util.List;

import lombok.Data;

@Data
public class NotificationGroupedBySenderDto {
    
    private final String senderFullName;
    
    private final List<NotificationResponse> notifications;
    
    private final int numberOfUnread;

    public NotificationGroupedBySenderDto(String senderFullName, List<NotificationResponse> notifications, int numberOfUnread) {
        this.senderFullName = senderFullName;
        this.notifications = notifications;
        this.numberOfUnread = numberOfUnread;
    }
}
