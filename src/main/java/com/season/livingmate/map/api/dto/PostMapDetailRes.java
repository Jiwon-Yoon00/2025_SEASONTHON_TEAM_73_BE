package com.season.livingmate.map.api.dto;

import com.season.livingmate.post.domain.Post;
import com.season.livingmate.user.domain.User;
import com.season.livingmate.user.domain.UserProfile;
import com.season.livingmate.user.domain.Gender;
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

        String smoking,

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
                userProfile != null ? userProfile.getSmoking().name() : null,
                userProfile != null ? userProfile.getWorkDays() : null,
                userProfile != null ? userProfile.getGoWorkTime() : null
        );
    }
}
