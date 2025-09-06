package com.season.livingmate.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CountRange {
	ZERO("0회"),
	ONE_TO_THREE("1~3회"),
	MORE_THAN_FOUR("4회 이상");

	private final String description;


	CountRange(String description) {
		this.description = description;
	}
	@JsonValue
	public String getDescription() {
		return description;
	}

	@JsonCreator
	public static CountRange fromString(String value) {
		if (value == null) return null;
		try {
			return CountRange.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid EarphoneUsage: " + value);
		}
	}
}
