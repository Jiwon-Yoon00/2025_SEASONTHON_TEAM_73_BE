package com.season.livingmate.post.api.dto.res;

import com.season.livingmate.post.domain.Post;
import com.season.livingmate.user.domain.Gender;
import com.season.livingmate.user.domain.User;
import com.season.livingmate.user.domain.UserProfile;

import java.time.LocalDateTime;

public record PostListRes(

        Long id,

        String imageUrl,

        String title,

        String roomType,

        int deposit,

        int monthlyRent,

        String region,

        Gender userGender,

        Object smoking,

        LocalDateTime availableDate
) {
        public static PostListRes from(Post p) {

                User user = p.getUser();
                UserProfile userProfile = user.getUserProfile();

                return new PostListRes(
                        p.getPostId(),
                        p.getImageUrl(),
                        p.getTitle(),
                        p.getRoomType().getKoreanName(),
                        p.getDeposit(),
                        p.getMonthlyRent(),
                        p.getRegionLabel(),
                        user.getGender(),
                        userProfile == null ? "정보 없음"
                                : (userProfile.isSmoking() ? "흡연" : "비흡연"),
                        p.getAvailableDate());
        }
}
