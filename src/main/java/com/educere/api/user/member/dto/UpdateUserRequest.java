package com.educere.api.user.member.dto;

import com.educere.api.user.auth.dto.AddressRequest;
import lombok.Getter;

@Getter
public class UpdateUserRequest {
    private String firstName;
    private String middleName;
    private String lastName;
    private String institutionName;
    private String linkedin;
    private String github;
    private String twitter;
    private String facebook;
    private String phoneOne;
    private String phoneTwo;
    private String phoneThree;
    private String website;
    private String bio;
    private String dp;
    private String wall;
    private AddressRequest addressRequest;
}
