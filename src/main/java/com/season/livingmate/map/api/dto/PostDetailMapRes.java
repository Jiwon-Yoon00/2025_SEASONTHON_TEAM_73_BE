package com.season.livingmate.map.api.dto;

import com.season.livingmate.map.util.CoordinateUtil;
import com.season.livingmate.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 상세보기용 200m 반경 지도 DTO")
public record PostDetailMapRes(
        @Schema(description = "게시글 ID", example = "101")
        Long id,

        @Schema(description = "제목", example = "역세권 원룸, 풀옵션")
        String title,

        @Schema(description = "정확한 주소", example = "서울특별시 강남구 테헤란로 123")
        String exactAddress,

        @Schema(description = "정확한 위도", example = "37.4979")
        Double exactLatitude,

        @Schema(description = "정확한 경도", example = "127.0276")
        Double exactLongitude,

        @Schema(description = "200m 반경 표시용 위도", example = "37.4985")
        Double displayLatitude,

        @Schema(description = "200m 반경 표시용 경도", example = "127.0281")
        Double displayLongitude

) {
    public static PostDetailMapRes from(Post post) {
        double exactLat = post.getGeoPoint().getLatitude();
        double exactLng = post.getGeoPoint().getLongitude();

        CoordinateUtil.RandomCoordinate displayCoord = CoordinateUtil.generateRandomCoordinate(exactLat, exactLng, 200);

        return new PostDetailMapRes(
                post.getPostId(),
                post.getTitle(),
                post.getLocation(),
                exactLat,
                exactLng,
                displayCoord.latitude(),
                displayCoord.longitude()
        );
    }
}
