package com.educere.api.experties.dto;

import lombok.Data;

@Data
public class ExpertiesRequest {

    private String name;

    private String category;

    private long experience;

    private String description;
}
