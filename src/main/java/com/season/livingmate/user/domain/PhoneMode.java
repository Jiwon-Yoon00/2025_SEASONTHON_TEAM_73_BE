package com.season.livingmate.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PhoneMode {
    SILENT("무음"),
    VIBRATE("진동"),
    SOUND("소리");

    private final String description;


    PhoneMode(String description) {
        this.description = description;
    }
    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static PhoneMode fromString(String value) {
        if (value == null) return null;
        try {
            return PhoneMode.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid EarphoneUsage: " + value);
        }
    }
}
