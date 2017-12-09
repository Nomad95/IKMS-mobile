package com.pollub.ikms.ikms_mobile.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Created by Igor on 2017-12-09.
 */

@Data
public class PhoneNumberResponse {

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("phoneNumber")
    private String phoneNumber;
}
