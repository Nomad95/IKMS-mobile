package com.pollub.ikms.ikms_mobile.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


import java.util.List;

@Data
public class NotificationGroupedBySender {

    @JsonProperty("senderFullName")
    private String senderFullName;

    @JsonProperty("notifications")
    private List<NotificationResponse> notifications;

    @JsonProperty("numberOfUnread")
    private int numberOfUnread;
}
