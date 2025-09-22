package com.season.livingmate.domain.map.api.dto;

import com.season.livingmate.domain.post.domain.Post;
import com.season.livingmate.domain.user.domain.User;
import com.season.livingmate.domain.user.domain.UserProfile;
import com.season.livingmate.domain.user.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "지도 마커 클릭 시 표시할 게시글 상세 정보")
public record PostMapDetailRes(
        Long id,

        String imageUrl,

        int deposit,

        int monthlyRent,

        String region,

        String roomType,

        int washroomCount,

        Gender userGender,

        Object smoking,

        List<String> workDays,

        String goWorkTime
) {
    public static PostMapDetailRes from(Post post) {
        User user = post.getUser();
        UserProfile userProfile = user.getUserProfile();

        return new PostMapDetailRes(
                post.getPostId(),
                post.getImageUrl(),
                post.getDeposit(),
                post.getMonthlyRent(),
                post.getRegionLabel(),
                post.getRoomType().getKoreanName(),
                post.getWashroomCount(),
                user.getGender(),
                userProfile == null ? "정보 없음"
                        : (userProfile.isSmoking() ? "흡연" : "비흡연"),
                userProfile != null ? userProfile.getWorkDays() : List.of(),
                userProfile != null ? userProfile.getGoWorkTime() : "정보 없음"
        );
    }
}
