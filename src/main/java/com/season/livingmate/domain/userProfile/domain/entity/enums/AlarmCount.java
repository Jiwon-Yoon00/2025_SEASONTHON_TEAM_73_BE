package com.season.livingmate.domain.userProfile.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AlarmCount {
	ONCE("1회"),  TWICE("2회"),
	THREE_OR_MORE("3회 이상"), UNKNOWN("미작성");

	private final String description;


	AlarmCount(String description) {
		this.description = description;
	}

	@JsonValue
	public String getDescription() {
		return description;
	}

	@JsonCreator
	public static AlarmCount fromString(String value) {
		if (value == null) return null;
		try {
			return AlarmCount.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid EarphoneUsage: " + value);
		}
	}
}
