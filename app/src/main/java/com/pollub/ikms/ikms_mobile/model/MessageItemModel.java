package com.pollub.ikms.ikms_mobile.model;

import com.pollub.ikms.ikms_mobile.response.UserDto;

import lombok.Data;

@Data
public class MessageItemModel {

    private Long id;

    private UserDto sender;

    private String title;

    private UserDto recipient;

    private String messageContents;

    private Boolean wasRead;

    private String recipientUsername;

    private String senderUsername;

    private String recipientFullName;

    private String senderFullName;

    private String dateOfSend;
}
