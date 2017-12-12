package com.pollub.ikms.ikms_mobile.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewMessageRequest {

    @JsonProperty("recipientUsername")
    String recipientUsername;

    @JsonProperty("title")
    String title;

    @JsonProperty("messageContents")
    String messageContents;
}
