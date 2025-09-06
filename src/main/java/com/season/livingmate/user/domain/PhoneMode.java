package com.season.livingmate.user.domain;

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

    public static PhoneMode fromString(String value) {
        if (value == null) return null;
        for (PhoneMode type : PhoneMode.values()) {
            if (type.name().equalsIgnoreCase(value) || type.getDescription().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid CountRange: " + value);
    }
}
