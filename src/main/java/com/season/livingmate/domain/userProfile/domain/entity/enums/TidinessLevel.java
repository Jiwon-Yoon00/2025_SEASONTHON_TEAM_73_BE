package com.season.livingmate.domain.userProfile.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TidinessLevel {
    LOW("항상 제자리에 둬요"), MEDIUM("대체로 정돈된 편이에요"), HIGH("어지르는 편이에요"), UNKNOWN("미작성");

    private final String description;


    TidinessLevel(String description) {
        this.description = description;
    }
    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static SensitivityLevel fromString(String value) {
        if (value == null) return null;
        try {
            return SensitivityLevel.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid EarphoneUsage: " + value);
        }
    }
}
