package com.season.livingmate.domain.userProfile.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WorkType {
    OFFICE("회사원"),
    STUDENT("학생"),
    REMOTE("재택근무"),
    FREELANCER("프리랜서"),
    UNEMPLOYED("무직")
    , UNKNOWN("미작성");


    private final String description;


    WorkType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static WorkType fromString(String value) {
        if (value == null) return null;
        try {
            return WorkType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid EarphoneUsage: " + value);
        }
    }

}
