package com.season.livingmate.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DishShare {
    SHARE("공용 식기"), PERSONAL("개인 식기");

    private final String description;


    DishShare(String description) {
        this.description = description;
    }
    @JsonValue
    public String getDescription() {
        return description;
    }
    @JsonCreator
    public static DishShare fromString(String value) {
        if (value == null) return null;
        for (DishShare type : DishShare.values()) {
            if (type.name().equalsIgnoreCase(value) || type.getDescription().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid CountRange: " + value);
    }
}
