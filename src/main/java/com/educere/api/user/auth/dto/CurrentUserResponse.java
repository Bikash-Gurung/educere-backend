package com.educere.api.user.auth.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CurrentUserResponse {
    private String name;

    private String email;

    private String phoneNumber;

    private String photo;

    private String bio;

    private String website;

    private String facebook;

    private String github;

    private String linkedIn;

    private String twitter;

    private List<String> roles;
}
