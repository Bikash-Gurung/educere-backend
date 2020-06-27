package com.educere.api.user.tutor.dto;

import lombok.Getter;

@Getter
public class RatingRequest {

    private Long userId;

    private Long star;

    private String comment;
}
