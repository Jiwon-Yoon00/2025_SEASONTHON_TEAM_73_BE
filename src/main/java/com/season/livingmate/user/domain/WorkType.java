package com.season.livingmate.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WorkType {
    OFFICE("회사원"),
    STUDENT("학생"),
    REMOTE("재택근무"),
    FREELANCER("프리랜서");


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
        for (WorkType type : WorkType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid WorkType: " + value);
    }

}
