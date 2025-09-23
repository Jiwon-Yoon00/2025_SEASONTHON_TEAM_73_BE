package com.season.livingmate.domain.post.api.dto.req;

import com.season.livingmate.domain.post.domain.entity.enums.HeatingType;
import com.season.livingmate.domain.post.domain.entity.enums.RoomType;
import com.season.livingmate.domain.user.domain.entity.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "게시글 작성 요청 DTO")
public record PostCreateReq(

        @Schema(description = "제목", example = "역세권 원룸, 풀옵션")
        String title,

        @Schema(description = "내용", example = "반려동물 불가, 엘리베이터 있음, 관리비 5만")
        String content,

        // 좌표
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
        @Schema(description = "보증금 분담 여부", example = "true")
        boolean depositShare,

        @Schema(description = "월세 분담 여부", example = "true")
        boolean rentShare,

        @Schema(description = "관리비 분담 여부", example = "false")
        boolean maintenanceShare,

        @Schema(description = "공과금 분담 여부", example = "true")
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

        @Schema(description = "방 개수", example = "2")
        int roomCount,

        @Schema(description = "모집 희망 성별", example = "[\"MALE\", \"FEMALE\"]")
        List<Gender> preferredGender
) {
}
