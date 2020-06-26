package com.educere.api.common.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum  UserType {
    TUTOR("tutor"),
    INSTITUTION("institution");

    private String value;

    UserType(String value) {
        this.value = value;
    }

    private static final Map<String, UserType> USER_TYPE_MAP = Arrays.stream(UserType.values())
            .collect(Collectors.toMap(UserType::getValue, Function.identity()));

    public static UserType get(String value) {
        return USER_TYPE_MAP.get(value);
    }
}
