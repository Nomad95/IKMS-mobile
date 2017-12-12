package com.pollub.ikms.ikms_mobile.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Created by ATyKondziu on 05.12.2017.
 */
@Data
public class MessageResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("senderId")
    private Long senderId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("recipientId")
    private Long recipientId;

    @JsonProperty("dateOfSend")
    private String dateOfSend;

    @JsonProperty("messageContent")
    private String messageContent;

    @JsonProperty("wasRead")
    private Boolean wasRead;

    @JsonProperty("recipientUsername")
    private String recipientUsername;

    @JsonProperty("senderUsername")
    private String senderUsername;

    @JsonProperty("recipientFullName")
    private String recipientFullName;

    @JsonProperty("senderFullName")
    private String senderFullName;
}
