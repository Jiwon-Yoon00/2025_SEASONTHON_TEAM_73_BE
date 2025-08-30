package com.season.livingmate.post.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class GeoPoint {

    private Double latitude; // 위도
    private Double longitude; // 경도
}
