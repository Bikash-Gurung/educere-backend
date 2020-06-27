package com.educere.api.user.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {

    private String country;
    private String state;
    private String city;
    private String street;
    private String zip;
    private String latitude;
    private String longitude;
}
