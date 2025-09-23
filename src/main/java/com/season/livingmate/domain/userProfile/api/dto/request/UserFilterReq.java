package com.season.livingmate.domain.userProfile.api.dto.request;

import com.season.livingmate.domain.userProfile.domain.entity.enums.CountRange;
import com.season.livingmate.domain.userProfile.domain.entity.enums.SensitivityLevel;
import com.season.livingmate.domain.userProfile.domain.entity.enums.TidinessLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "사용자 필터 요청 DTO")
public class UserFilterReq {

    @Schema(description = "흡연 여부", example = "false")
    private Boolean smoking; // 흡연 여부

    @Schema(description = "음주 횟수", example = "[\"ZERO\"]", nullable = true)
    private List<CountRange> alcoholCount; // 음주 횟수

    @Schema(description = "잠귀 민감도", example = "[\"LOW\", \"MEDIUM\"]", nullable = true)
    private List<SensitivityLevel> sleepLevel; // 잠귀 민감도

    @Schema(description = "반려동물", example = "[\"강아지\", \"고양이, \"물고기\"]", nullable = true)
    private List<String> pet;

    @Schema(description = "정리정돈 성향", example = "[\"LOW\", \"MEDIUM\"]", nullable = true)
    private List<TidinessLevel> tidinessLevel; // 정리정돈 성향
}
