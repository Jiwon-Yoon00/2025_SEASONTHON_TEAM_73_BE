package com.season.livingmate.gpt.api.dto.response;

import com.season.livingmate.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 추천 응답 DTO")
public record UserRecommendationResDto(
        UserBasicInfo user,
        String matchScore,
        String reason
){
    public record UserBasicInfo (
            Long id,
            String nickname,
            String profileImageUrl
    ){
        public static UserBasicInfo from(User user) {
            return new UserBasicInfo(
                    user.getId(),
                    user.getNickname(),
                    user.getUserProfile() != null ? user.getUserProfile().getProfileImageUrl() : null
            );
        }
    }
}
