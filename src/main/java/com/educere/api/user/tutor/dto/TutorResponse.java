package com.educere.api.user.tutor.dto;

import com.educere.api.entity.Address;
import lombok.Data;

import java.util.UUID;

@Data
public class TutorResponse {
    private Long id;

    private UUID referenceId;

    private String firstName;

    private String middleName;

    private String lastName;

    private Address address;

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

    private boolean status = true;
}
