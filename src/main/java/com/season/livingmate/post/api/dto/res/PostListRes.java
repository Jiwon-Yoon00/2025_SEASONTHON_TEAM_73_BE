package com.season.livingmate.post.api.dto.res;

import com.season.livingmate.post.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record PostListRes(

        @Schema(description = "게시글 ID", example = "101")
        Long id,

        @Schema(description = "제목", example = "역세권 원룸, 풀옵션")
        String title,

        @Schema(description = "지역(서울시 구 동)", example = "서울시 노원구 중계동")
        String region,

        @Schema(description = "입주 가능일", example = "2025-09-10T00:00:00")
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
