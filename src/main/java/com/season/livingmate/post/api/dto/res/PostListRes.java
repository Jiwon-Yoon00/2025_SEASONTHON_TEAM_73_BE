package com.season.livingmate.post.api.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record PostListRes(

        @Schema(description = "게시글 ID", example = "101")
        Long id,

        @Schema(description = "제목", example = "역세권 원룸, 풀옵션")
        String title,

// 카카오맵 연결 후 활성화
//        @Schema(description = "위치(동 단위)", example = "역삼동")
//        String dong,

        @Schema(description = "입주 가능일", example = "2025-09-10T00:00:00")
        LocalDateTime availableDate
) {
}
