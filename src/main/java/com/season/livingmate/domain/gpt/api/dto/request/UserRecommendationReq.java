package com.season.livingmate.domain.gpt.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "사용자 추천 요청 DTO")
public record UserRecommendationReq(

        @NotEmpty(message = "정확히 3개의 항목을 선택해야 합니다.")
        @Size(min = 3, max = 3, message = "정확히 3개의 항목을 선택해야 합니다.")
        @Schema(description = "선택된 성향조사 항목 (3개)", example = "[\"smellLevel\", \"sleepLevel\", \"tidinessLevel\"]")
        List<String> selectedItems
) {
}
