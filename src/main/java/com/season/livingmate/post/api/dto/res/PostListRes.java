package com.season.livingmate.post.api.dto.res;

import com.season.livingmate.post.domain.Post;
import com.season.livingmate.user.domain.Gender;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public record PostListRes(

        Long id,

        String imageUrl,

        String title,

        String roomType,

        int deposit,

        int monthlyRent,

        String region,

        List<String> preferredGender,

        LocalDateTime availableDate
) {
        public static PostListRes from(Post p) {

                List<String> genders = p.getPreferredGender() != null ?
                        Arrays.asList(p.getPreferredGender().split(",")) :
                        List.of();

                return new PostListRes(
                        p.getPostId(),
                        p.getImageUrl(),
                        p.getTitle(),
                        p.getRoomType().getKoreanName(),
                        p.getDeposit(),
                        p.getMonthlyRent(),
                        p.getRegionLabel(),
                        genders,
                        p.getAvailableDate());
        }
}
