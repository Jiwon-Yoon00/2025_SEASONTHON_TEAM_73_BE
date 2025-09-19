package com.season.livingmate.main.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Schema(description = "대시보드 응답 dto")
@Builder
public record DashboardResDto(
        @Schema(description = "추천 사용자들 (성향조사 유)")
        RecommendedUsers recommendedUsers,

        @Schema(description = "내가 찜한 유저들")
        LikedUsers likedUsers,

        @Schema(description = "성향조사 완료 여부")
        boolean isPersonalitySurveyCompleted
) {

    @Schema(description = "추천 사용자들")
    public record RecommendedUsers(
            @Schema(description = "추천 사용자 목록")
            List<RecommendedUserCard> users
    ){}

    @Schema(description = "내가 찜한 사용자들")
    public record LikedUsers(
            @Schema(description = "찜한 사용자 목록")
            List<LikedUserCard> users
    ){}

    @Schema(description = "추천 사용자 카드")
    public record RecommendedUserCard(
            @Schema(description = "사용자 ID")
            Long userId,

            @Schema(description = "닉네임")
            String nickname,

            @Schema(description = "프로필 이미지 URL")
            String profileImageUrl,

            @Schema(description = "매칭 스코어")
            Integer matchScore,

            @Schema(description = "성별")
            String gender,

            @Schema(description = "나이")
            int age,

            @Schema(description = "흡연 여부")
            boolean smoking
    ){}

    @Schema(description = "찜한 사용자 카드")
    public record LikedUserCard(
            @Schema(description = "사용자 ID")
            Long userId,

            @Schema(description = "닉네임")
            String nickname,

            @Schema(description = "성별")
            String gender,

            @Schema(description = "나이")
            int age,

            @Schema(description = "흡연 여부")
            boolean smoking
    ){}
}
