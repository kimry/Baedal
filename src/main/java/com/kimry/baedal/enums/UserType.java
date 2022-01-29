package com.kimry.baedal.enums;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {

    @JsonProperty("customer")
    CUSTOMER("customer"),
    @JsonProperty("driver")
    DRIVER("driver");

    private String type;

    public static UserType value(String userType){
        if(userType.equals("customer")) {
            return CUSTOMER;
        }
        else if(userType.equals("driver"))
        {
            return DRIVER;
        }
        return null;
    }

}