package com.season.livingmate.domain.map.api.dto;

import com.season.livingmate.domain.map.util.CoordinateUtil;

import com.season.livingmate.domain.post.domain.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "지도용 게시글 마커 DTO")
public record PostMapRes(
        Long id,

        String title,

        @Schema(description = "표시용 위도 (200m 반경 랜덤)", example = "37.4979")
        Double displayLatitude,

        Double displayLongitude
) {
    public static PostMapRes from(Post post) {
        CoordinateUtil.RandomCoordinate randomCoord = CoordinateUtil.generateRandomCoordinate(
                post.getGeoPoint().getLatitude(),
                post.getGeoPoint().getLongitude(),
                200 // 200m 반경
        );

        return new PostMapRes(
                post.getPostId(),
                post.getTitle(),
                randomCoord.latitude(),
                randomCoord.longitude()
        );
    }
}