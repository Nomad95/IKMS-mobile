package com.pollub.ikms.ikms_mobile.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccount {

    @JsonProperty("email")
    private String email;

    @JsonProperty("country")
    private String country;

    @JsonProperty("nick")
    private String nick;

    @JsonProperty("authorities")
    private String authorities;

}
