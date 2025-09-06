package com.season.livingmate.user.domain;

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

	public static CountRange fromString(String value) {
		if (value == null) return null;
		for (CountRange type : CountRange.values()) {
			if (type.name().equalsIgnoreCase(value) || type.getDescription().equals(value)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid CountRange: " + value);
	}
}
