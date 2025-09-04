package com.season.livingmate.map.api.dto;

import com.season.livingmate.map.util.CoordinateUtil;
import com.season.livingmate.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "게시글 상세보기용 200m 반경 지도 DTO")
public record PostDetailMapRes(
        Long id,

        String title,

        String exactAddress,

        Double exactLatitude,

        Double exactLongitude,

        Double displayLatitude,

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
