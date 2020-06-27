package com.educere.api.experties.dto;

import com.educere.api.entity.Tutor;
import lombok.Data;

@Data
public class ExpertiesResponse {

    private Long id;

    private String name;

    private String category;

    private long experience;

    private String description;
}
