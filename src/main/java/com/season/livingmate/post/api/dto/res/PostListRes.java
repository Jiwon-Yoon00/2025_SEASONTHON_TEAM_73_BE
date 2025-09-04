package com.season.livingmate.post.api.dto.res;

import com.season.livingmate.post.domain.Post;
import com.season.livingmate.user.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record PostListRes(

        Long id,

        String imageUrl,

        String title,

        String roomType,

        int deposit,

        int monthlyRent,

        String region,

        Gender gender,

        LocalDateTime availableDate
) {
        public static PostListRes from(Post p) {
                return new PostListRes(
                        p.getPostId(),
                        p.getImageUrl(),
                        p.getTitle(),
                        p.getRoomType().getKoreanName(),
                        p.getDeposit(),
                        p.getMonthlyRent(),
                        p.getRegionLabel(),
                        p.getUser().getGender(),
                        p.getAvailableDate());
        }
}
