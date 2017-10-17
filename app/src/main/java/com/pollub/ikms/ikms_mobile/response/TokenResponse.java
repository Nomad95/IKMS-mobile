package com.pollub.ikms.ikms_mobile.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponse {

    @JsonProperty("token")
    private String token;
}
