package com.pollub.ikms.ikms_mobile.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Role {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;
}
