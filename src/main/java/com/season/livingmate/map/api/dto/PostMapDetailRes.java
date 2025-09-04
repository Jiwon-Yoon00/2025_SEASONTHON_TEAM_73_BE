package com.season.livingmate.map.api.dto;

import com.season.livingmate.post.domain.Post;
import com.season.livingmate.user.domain.User;
import com.season.livingmate.user.domain.UserProfile;
import com.season.livingmate.user.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "지도 마커 클릭 시 표시할 게시글 상세 정보")
public record PostMapDetailRes(
        @Schema(description = "게시글 ID", example = "101")
        Long id,

        @Schema(description = "보증금(만원)", example = "1000")
        int deposit,

        @Schema(description = "월세(만원)", example = "50")
        int monthlyRent,

        @Schema(description = "지역(서울시 구 동)", example = "서울시 노원구 중계동")
        String region,

        @Schema(description = "방 구조", example = "ONE_ROOM")
        String roomType,

        @Schema(description = "화장실 개수", example = "1")
        int washroomCount,

        @Schema(description = "게시자 성별", example = "MALE")
        Gender userGender,

        @Schema(description = "흡연 여부", example = "NON_SMOKER")
        String smoking,

        @Schema(description = "출근 요일", example = "월,화,수,목,금")
        List<String> workDays,

        @Schema(description = "출근 시간", example = "09:00")
        String goWorkTime
) {
    public static PostMapDetailRes from(Post post) {
        User user = post.getUser();
        UserProfile userProfile = user.getUserProfile();
        
        return new PostMapDetailRes(
                post.getPostId(),
                post.getDeposit(),
                post.getMonthlyRent(),
                post.getRegionLabel(),
                post.getRoomType().name(),
                post.getWashroomCount(),
                user.getGender(),
                userProfile != null ? userProfile.getSmoking().toString() : null,
                userProfile != null ? userProfile.getWorkDays() : null,
                userProfile != null ? userProfile.getGoWorkTime() : null
        );
    }
}
