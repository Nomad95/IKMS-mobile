package com.pollub.ikms.ikms_mobile.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

import lombok.Data;

@Data
public class UserDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("enabled")
    private boolean enabled;

    @JsonProperty("lastLogged")
    private Date lastLogged;

    @JsonProperty("createdDate")
    private Date createdDate;

    @JsonProperty("role")
    private Role role;
}
