package com.season.livingmate.geo.api.dto;

public record GeoResult(
        String address,
        Double lat,
        Double lng
) {
}
