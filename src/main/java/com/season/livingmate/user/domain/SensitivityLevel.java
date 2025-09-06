package com.season.livingmate.user.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SensitivityLevel {
	LOW("둔감해요"), MEDIUM("보통이에요"), HIGH("예민한 편이에요");

	private final String description;


	SensitivityLevel(String description) {
		this.description = description;
	}
	@JsonValue
	public String getDescription() {
		return description;
	}

	public static SensitivityLevel fromString(String value) {
		if (value == null) return null;
		for (SensitivityLevel type : SensitivityLevel.values()) {
			if (type.name().equalsIgnoreCase(value) || type.getDescription().equals(value)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid CountRange: " + value);
	}
}
