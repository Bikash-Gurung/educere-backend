package com.educere.api.rating.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingResponse {
    private Long id;
    private Long star;
    private String comment;
}
