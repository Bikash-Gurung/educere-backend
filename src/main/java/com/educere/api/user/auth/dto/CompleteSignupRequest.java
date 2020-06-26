package com.educere.api.user.auth.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CompleteSignupRequest {

    private String linkedin;
    private String github;
    private String twitter;
    private String facebook;
    private String phoneOne;
    private String phoneTwo;
    private String phoneThree;
    private String website;
    private String bio;
    private String db;
    private String wall;
    private AddressRequest address;
}
