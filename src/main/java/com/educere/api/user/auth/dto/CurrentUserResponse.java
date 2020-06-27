package com.educere.api.user.auth.dto;

import com.educere.api.entity.Address;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
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

    private Address address;

    @Getter
    @Setter
    public static class Address {
        private String country;

        private String state;

        private String city;

        private String street;

        private String zip;

        private String latitude;

        private String longitude;
    }
}
