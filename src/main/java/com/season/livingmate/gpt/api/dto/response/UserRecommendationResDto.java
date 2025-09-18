package com.season.livingmate.gpt.api.dto.response;

import com.season.livingmate.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "사용자 추천 응답 DTO")
public record UserRecommendationResDto(
        UserBasicInfo user,
        Integer matchScore,
        List<String> reasonItems,
        List<Integer> reasonScores
){
    public record UserBasicInfo (
            Long id,
            String nickname,
            String profileImageUrl,
            String gender,
            int age,
            boolean smoking
    ){
        public static UserBasicInfo from(User user) {
            return new UserBasicInfo(
                    user.getId(),
                    user.getNickname(),
                    user.getUserProfile() != null ? user.getUserProfile().getProfileImageUrl() : null,
                    user.getGender().getDescription(),
                    user.getAge(),
                    user.getUserProfile() != null ? user.getUserProfile().isSmoking() : false
            );
        }
    }
}

