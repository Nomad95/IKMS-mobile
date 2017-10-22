package com.pollub.ikms.ikms_mobile.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("employeeRole")
    private String employeeRole;

    @JsonProperty("nip")
    private String nip;
}
