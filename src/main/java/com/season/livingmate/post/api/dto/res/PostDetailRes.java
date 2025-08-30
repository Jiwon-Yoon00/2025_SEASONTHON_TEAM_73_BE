package com.season.livingmate.post.api.dto.res;

import com.season.livingmate.post.domain.HeatingType;
import com.season.livingmate.post.domain.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "게시글 상세 조회 응답 DTO")
public record PostDetailRes(

        @Schema(description = "게시글 ID", example = "101")
        Long id,

        @Schema(description = "제목", example = "역세권 원룸, 풀옵션")
        String title,

        @Schema(description = "내용", example = "반려동물 불가, 관리비 5만")
        String content,

        @Schema(description = "대표 이미지 URL", example = "https://img.example.com/room1.jpg")
        String imageUrl,

        @Schema(description = "위도", example = "37.4979")
        Double latitude,

        @Schema(description = "경도", example = "127.0276")
        Double longitude,

        @Schema(description = "실제 주소", example = "서울특별시 강남구 테헤란로 123")
        String location,

        @Schema(description = "방 종류", example = "ONE_ROOM")
        RoomType roomType,

        @Schema(description = "보증금(만원)", example = "1000")
        int deposit,

        @Schema(description = "월세(만원)", example = "50")
        int monthlyRent,

        @Schema(description = "관리비(만원)", example = "5")
        int maintenanceFee,

        // 분담(불리언 4개)
        @Schema(description = "보증금 분담", example = "true")
        boolean depositShare,

        @Schema(description = "월세 분담", example = "true")
        boolean rentShare,

        @Schema(description = "관리비 분담", example = "false")
        boolean maintenanceShare,

        @Schema(description = "공과금 분담", example = "true")
        boolean utilitiesShare,

        @Schema(description = "해당 층", example = "3")
        int floor,

        @Schema(description = "건물 총 층수", example = "15")
        int buildingFloor,

        @Schema(description = "전용면적(평수)", example = "18")
        int areaSize,

        @Schema(description = "난방 방식", example = "CENTRAL")
        HeatingType heatingType,

        @Schema(description = "엘리베이터 여부", example = "true")
        boolean hasElevator,

        @Schema(description = "입주 가능일", example = "2025-09-10T00:00:00")
        LocalDateTime availableDate,

        @Schema(description = "최소 거주 개월", example = "6")
        int minStayMonths,

        @Schema(description = "최대 거주 개월", example = "24")
        int maxStayMonths,

        @Schema(description = "화장실 개수", example = "1")
        int washroomCount,

        @Schema(description = "방 개수", example = "1")
        int roomCount,

        @Schema(description = "생성일시", example = "2025-08-30T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "수정일시", example = "2025-08-30T13:00:00")
        LocalDateTime updatedAt
) {
}
