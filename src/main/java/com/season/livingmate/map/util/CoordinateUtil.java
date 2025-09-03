package com.season.livingmate.map.util;

public class CoordinateUtil {
    
    // 받은 주소로부터 200m 반경 랜덤 좌표 생성
    public static RandomCoordinate generateRandomCoordinate(double centerLat, double centerLng, int radiusMeters) {
        // 지구 반지름
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

    public record RandomCoordinate(double latitude, double longitude) {}
}
