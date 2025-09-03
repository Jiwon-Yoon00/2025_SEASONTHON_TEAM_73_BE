package com.season.livingmate.map.api.dto;

import com.season.livingmate.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "지도용 게시글 마커 DTO")
public record PostMapRes(
        @Schema(description = "게시글 ID", example = "101")
        Long id,

        @Schema(description = "제목", example = "역세권 원룸, 풀옵션")
        String title,

        @Schema(description = "표시용 위도 (200m 반경 랜덤)", example = "37.4979")
        Double displayLatitude,

        @Schema(description = "표시용 경도 (200m 반경 랜덤)", example = "127.0276")
        Double displayLongitude
) {
    public static PostMapRes from(Post post) {
        // 실제 좌표에서 200m 반경 랜덤 좌표 생성
        RandomCoordinate randomCoord = generateRandomCoordinate(
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

    private static RandomCoordinate generateRandomCoordinate(double centerLat, double centerLng, int radiusMeters) {
        // 지구 반지름 (미터)
        final double EARTH_RADIUS = 6371000;
        
        // 랜덤 각도와 거리 생성
        double randomAngle = Math.random() * 2 * Math.PI;
        double randomRadius = Math.random() * radiusMeters;
        
        // 위도, 경도로 변환
        double deltaLat = randomRadius * Math.cos(randomAngle) / EARTH_RADIUS * (180 / Math.PI);
        double deltaLng = randomRadius * Math.sin(randomAngle) / (EARTH_RADIUS * Math.cos(Math.toRadians(centerLat))) * (180 / Math.PI);
        
        double newLat = centerLat + deltaLat;
        double newLng = centerLng + deltaLng;
        
        return new RandomCoordinate(newLat, newLng);
    }

    private record RandomCoordinate(double latitude, double longitude) {}
}