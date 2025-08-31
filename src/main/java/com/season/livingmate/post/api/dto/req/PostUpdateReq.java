package com.season.livingmate.post.api.dto.req;

import com.season.livingmate.post.domain.HeatingType;
import com.season.livingmate.post.domain.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "게시글 수정 요청 DTO")
public record PostUpdateReq(

        @Schema(description = "제목", example = "역세권 원룸, 풀옵션(가격인하)")
        String title,

        @Schema(description = "내용", example = "보증금 조정 가능")
        String content,

        @Schema(description = "대표 이미지 URL", example = "https://img.example.com/room1-new.jpg")
        String imageUrl,

        // 좌표
        @Schema(description = "위도", example = "37.4985")
        Double latitude,

        @Schema(description = "경도", example = "127.0281")
        Double longitude,

        @Schema(description = "실제 주소", example = "서울 강남구 역삼로 321")
        String location,

        @Schema(description = "방 종류", example = "TWO_ROOM")
        RoomType roomType,

        @Schema(description = "보증금(만원)", example = "900")
        Integer deposit,

        @Schema(description = "월세(만원)", example = "48")
        Integer monthlyRent,

        @Schema(description = "관리비(만원)", example = "4")
        Integer maintenanceFee,

        // 분담(불리언 4개)
        @Schema(description = "보증금 분담 여부", example = "true")
        Boolean depositShare,
        @Schema(description = "월세 분담 여부", example = "true")
        Boolean rentShare,
        @Schema(description = "관리비 분담 여부", example = "false")
        Boolean maintenanceShare,
        @Schema(description = "공과금 분담 여부", example = "true")
        Boolean utilitiesShare,

        @Schema(description = "해당 층", example = "4")
        Integer floor,

        @Schema(description = "건물 총 층수", example = "16")
        Integer buildingFloor,

        @Schema(description = "전용면적(평수)", example = "20")
        Integer areaSize,

        @Schema(description = "난방 방식", example = "INDIVIDUAL")
        HeatingType heatingType,

        @Schema(description = "엘리베이터 여부", example = "true")
        Boolean hasElevator,

        @Schema(description = "입주 가능일", example = "2025-10-01T00:00:00")
        LocalDateTime availableDate,

        @Schema(description = "최소 거주 개월", example = "6")
        Integer minStayMonths,

        @Schema(description = "최대 거주 개월", example = "36")
        Integer maxStayMonths,

        @Schema(description = "화장실 개수", example = "1")
        Integer washroomCount,

        @Schema(description = "방 개수", example = "2")
        Integer roomCount
) {
}
