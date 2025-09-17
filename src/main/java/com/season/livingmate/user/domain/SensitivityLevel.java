package com.season.livingmate.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SensitivityLevel {
	LOW("둔감해요"), MEDIUM("보통이에요"), HIGH("예민한 편이에요"), UNKNOWN("미작성");

	private final String description;


	SensitivityLevel(String description) {
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
