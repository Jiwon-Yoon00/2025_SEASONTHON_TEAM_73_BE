package com.season.livingmate.user.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    MALE("남"), FEMALE("여");

    private final String description;


    Gender(String description) {
        this.description = description;
    }
    @JsonValue
    public String getDescription() {
        return description;
    }

    public static Gender fromString(String value) {
        if (value == null) return null;
        for (Gender type : Gender.values()) {
            if (type.name().equalsIgnoreCase(value) || type.getDescription().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid CountRange: " + value);
    }
}
