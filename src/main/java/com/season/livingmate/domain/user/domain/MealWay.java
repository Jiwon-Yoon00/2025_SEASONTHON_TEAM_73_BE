package com.season.livingmate.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MealWay {
    COOK("주로 해 먹어요"), ORDER("주로 외식/배달음식 먹어요"), UNKNOWN("미작성");

    private final String description;


    MealWay(String description) {
        this.description = description;
    }
    @JsonValue
    public String getDescription() {
        return description;
    }
    @JsonCreator
    public static MealWay fromString(String value) {
        if (value == null) return null;
        try {
            return MealWay.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid EarphoneUsage: " + value);
        }
    }
}
