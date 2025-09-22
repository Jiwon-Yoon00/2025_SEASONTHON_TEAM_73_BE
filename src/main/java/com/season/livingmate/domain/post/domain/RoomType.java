package com.season.livingmate.domain.post.domain;

public enum RoomType {
    ONE_ROOM("원룸"),
    TWO_ROOM("투룸"),
    VILLA("빌라"),
    APARTMENT("아파트"),
    OFFICETEL("오피스텔");

    private final String koreanName;

    RoomType(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }
}

