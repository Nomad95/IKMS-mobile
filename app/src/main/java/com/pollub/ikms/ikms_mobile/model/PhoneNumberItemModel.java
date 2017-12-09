package com.pollub.ikms.ikms_mobile.model;

import lombok.Data;

/**
 * Created by ATyKondziu on 05.11.2017.
 */
@Data
public class PhoneNumberItemModel {

    private Long id;

    private String fullName;

    private String number;

    public PhoneNumberItemModel(Long id, String fullName, String number) {
        this.id = id;
        this.fullName = fullName;
        this.number = number;
    }
}
