package com.season.livingmate.post.api.dto.req;

import com.season.livingmate.post.domain.RoomType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record PostSearchReq(
        @Schema(description = "제목 또는 본문에 포함된 키워드")
        String keyword,

        @Schema(description = "최소 보증금")
        Integer minDeposit,

        @Schema(description = "최대 보증금")
        Integer maxDeposit,

        @Schema(description = "최소 월세+관리비")
        Integer minMonthlyCost,

        @Schema(description = "최대 월세+관리비")
        Integer maxMonthlyCost,

        @Schema(description = "방 형태")
        List<RoomType> roomTypes,

        @Schema(description = "선택한 동 목록")
        List<String> dongs
) {
}
