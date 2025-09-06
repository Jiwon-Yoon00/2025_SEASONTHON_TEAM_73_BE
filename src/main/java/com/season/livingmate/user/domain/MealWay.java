package com.season.livingmate.user.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MealWay {
    COOK("주로 해 먹어요"), ORDER("주로 외식/배달음식 먹어요");

    private final String description;


    MealWay(String description) {
        this.description = description;
    }
    @JsonValue
    public String getDescription() {
        return description;
    }
    public static MealWay fromString(String value) {
        if (value == null) return null;
        for (MealWay type : MealWay.values()) {
            if (type.name().equalsIgnoreCase(value) || type.getDescription().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid CountRange: " + value);
    }
}
