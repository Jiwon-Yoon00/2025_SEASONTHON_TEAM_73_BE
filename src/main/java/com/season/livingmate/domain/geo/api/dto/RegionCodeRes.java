package com.season.livingmate.domain.geo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record RegionCodeRes(List<Doc> documents) {
    public record Doc(
            @JsonProperty("region_1depth_name") String region1depthName,
            @JsonProperty("region_2depth_name") String region2depthName,
            @JsonProperty("region_3depth_name") String region3depthName
    ){}
}
