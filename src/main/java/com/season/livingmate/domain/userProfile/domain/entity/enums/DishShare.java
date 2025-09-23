package com.season.livingmate.domain.userProfile.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DishShare {
    SHARE("공용 식기"), PERSONAL("개인 식기"), UNKNOWN("미작성");

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
        try {
            return DishShare.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid EarphoneUsage: " + value);
        }
    }
}
