package com.season.livingmate.domain.geo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

// 카카오 주소검색 응답을 파싱하기 위한 dto
public record KakaoAddressRes(
        List<Document> documents
) {
    public record Document(
            @JsonProperty("address_name") String addressName, // 지번 전체주소
            String x, // 경도
            String y, // 위도
            @JsonProperty("road_address") RoadAddress roadAddress,
            Address address
    ){}
    public record RoadAddress(
            @JsonProperty("address_name") String addressName,
            @JsonProperty("region_1depth_name") String region1depthName, // 시/도
            @JsonProperty("region_2depth_name") String region2depthName, // 시/군/구
            @JsonProperty("region_3depth_name") String region3depthName, // 읍/면/동/법정동
            @JsonProperty("road_name") String roadName
    ){}

    public record Address(
            @JsonProperty("address_name") String addressName,
            @JsonProperty("region_1depth_name") String region1depthName,
            @JsonProperty("region_2depth_name") String region2depthName,
            @JsonProperty("region_3depth_name") String region3depthName
    ){}
}
