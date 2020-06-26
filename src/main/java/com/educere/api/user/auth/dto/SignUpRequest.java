package com.educere.api.user.auth.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class SignUpRequest {

    private String firstName;

    private String middleName;

    private String lastName;

    private String institutionName;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;

    private String userType;
}