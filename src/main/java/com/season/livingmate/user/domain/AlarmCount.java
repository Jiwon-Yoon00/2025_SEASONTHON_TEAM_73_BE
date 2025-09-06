package com.season.livingmate.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AlarmCount {
	ONCE("1회"),  TWICE("2회"),
	THREE_OR_MORE("3회 이상");

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
		for (AlarmCount type : AlarmCount.values()) {
			if (type.name().equalsIgnoreCase(value) || type.getDescription().equals(value)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid AlarmCount: " + value);
	}
}
