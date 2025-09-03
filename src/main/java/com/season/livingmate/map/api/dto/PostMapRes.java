package com.season.livingmate.map.api.dto;

import com.season.livingmate.map.util.CoordinateUtil;
import com.season.livingmate.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "지도용 게시글 마커 DTO")
public record PostMapRes(
        @Schema(description = "게시글 ID", example = "1")
        Long id,

        @Schema(description = "제목", example = "역세권 원룸, 풀옵션")
        String title,

        @Schema(description = "표시용 위도 (200m 반경 랜덤)", example = "37.4979")
        Double displayLatitude,

        @Schema(description = "표시용 경도 (200m 반경 랜덤)", example = "127.0276")
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