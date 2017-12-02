package com.pollub.ikms.ikms_mobile.response;

/**
 * Created by ATyKondziu on 03.11.2017.
 */

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class NotificationResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("content")
    private String content;

    @JsonProperty("dateOfSend")
    private Date dateOfSend;

    @JsonProperty("wasRead")
    private Boolean wasRead;

    @JsonProperty("priority")
    private String priority;

}


