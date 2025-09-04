package com.season.livingmate.post.api.dto.res;

import com.season.livingmate.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record PostListRes(

        Long id,

        String title,

        String region,

        LocalDateTime availableDate
) {
        public static PostListRes from(Post p) {
                return new PostListRes(
                        p.getPostId(),
                        p.getTitle(),
                        p.getRegionLabel(),
                        p.getAvailableDate());
        }
}
