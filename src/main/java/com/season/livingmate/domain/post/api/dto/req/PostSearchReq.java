package com.season.livingmate.domain.post.api.dto.req;

import com.season.livingmate.domain.post.domain.entity.enums.RoomType;
import com.season.livingmate.domain.userProfile.api.dto.request.UserFilterReq;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

public record PostSearchReq(
        @Schema(
            description = "제목 또는 본문에 포함된 키워드", 
            example = "원룸"
        )
        String keyword,

        @Schema(
            description = "최소 보증금 (만원)", 
            example = "500"
        )
        Integer minDeposit,

        @Schema(
            description = "최대 보증금 (만원)", 
            example = "1500"
        )
        Integer maxDeposit,

        @Schema(
            description = "최소 월세+관리비 (만원)", 
            example = "30"
        )
        Integer minMonthlyCost,

        @Schema(
            description = "최대 월세+관리비 (만원)", 
            example = "80"
        )
        Integer maxMonthlyCost,

        @Schema(
            description = "방 형태 (한국어로 입력)", 
            example = "원룸",
            allowableValues = {"원룸", "투룸", "빌라", "아파트", "오피스텔"}
        )
        List<String> roomTypes,

        @Schema(
            description = "선택한 동 목록", 
            example = "역삼동"
        )
        List<String> dongs,

        @Schema(
                description = "사용자 필터 조건"
        ) UserFilterReq userFilter
) {
    // 한국어 String을 RoomType enum으로 변환
    public List<RoomType> getRoomTypeEnums() {
        if (roomTypes == null || roomTypes.isEmpty()) {
            return null;
        }
        
        return roomTypes.stream()
                .map(this::convertToRoomType)
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toList());
    }
    
    private RoomType convertToRoomType(String koreanName) {
        if (koreanName == null || koreanName.trim().isEmpty()) {
            return null;
        }
        
        String normalized = koreanName.trim();
        
        // 기존 koreanName과 비교
        for (RoomType roomType : RoomType.values()) {
            if (roomType.getKoreanName().equals(normalized)) {
                return roomType;
            }
        }
        
        return null; // 매칭되지 않으면 null 반환
    }
}
