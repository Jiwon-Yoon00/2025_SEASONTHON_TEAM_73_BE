package com.season.livingmate.user.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EarphoneUsage {
	ALAWAYS("항상"), NIGHT_ONLY("밤에만"),
	NONE("안 써요");

	private final String description;


	EarphoneUsage(String description) {
		this.description = description;
	}

	@JsonValue
	public String getDescription() {
		return description;
	}

	@JsonCreator
	public static EarphoneUsage fromString(String value) {
		if (value == null) return null;
		for (EarphoneUsage type : EarphoneUsage.values()) {
			if (type.name().equalsIgnoreCase(value) ) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid CountRange: " + value);
	}
}
