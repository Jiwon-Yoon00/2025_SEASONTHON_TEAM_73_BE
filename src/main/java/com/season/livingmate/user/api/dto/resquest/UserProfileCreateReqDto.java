package com.season.livingmate.user.api.dto.resquest;

import com.season.livingmate.user.domain.*;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "유저프로필 생성 요청 DTO")
public class UserProfileCreateReqDto {

    // 식사 습관
    @Schema(description = "요리 빈도", example = "COOK",allowableValues = {"COOK", "ORDER"})
    @NotNull
    private MealWay cookingCount;

    @Schema(description = "냄새 민감도", example = "HIGH",allowableValues = {"LOW", "MEDIUM", "HIGH"})
    @NotNull
    private SensitivityLevel smellLevel;

    @Schema(description = "주 음주 횟수", example = "ZERO",allowableValues = {"ZERO", "ONE_TO_THREE", "MORE_THAN_FOUR"})
    @NotNull
    private CountRange alcoholCount;

    @Schema(description = "식기류 공유 여부", example = "SHARE",allowableValues = {"SHARE", "PERSONAL"})
    @NotNull
    private DishShare dishShare;


    // 소리 민감도
    @Schema(description = "잠귀 민감도", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    @NotNull
    private SensitivityLevel sleepLevel;

    @ArraySchema(schema = @Schema(description = "잠버릇", example = "SNORING, TEETH_GRINDING"))
    private List<@NotBlank String> sleepHabit;

    @Schema(description = "나의 휴대폰 모드", example = "SILENT" , allowableValues = {"VIBRATION", "SILENT", "SOUND"})
    @NotNull
    private PhoneMode phoneMode;

    @Schema(description = "나의 이어폰 사용", example = "NIGHT_ONLY" , allowableValues = {"ALAWAYS", "NIGHT_ONLY", "NONE"})
    @NotNull
    private EarphoneUsage earphoneUsage;

    // 청소 습관
    @Schema(description = "화장실 청소 빈도", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    @NotNull
    private SensitivityLevel bathroomCleaningLevel;

    @Schema(description = "정리정돈 성향", example = "LOW", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    @NotNull
    private SensitivityLevel tidinessLevel;

    @Schema(description = "흡연여부" , example = "false")
    @NotNull
    private boolean smoking;


    @ArraySchema(schema = @Schema(description = "반려동물 종류 목록", example = "강아지, 고양이"))
    private List<@NotBlank String> pet;

    @Schema(description = "질병 정보", example = "없음")
    @NotBlank
    private String disease;

    @Schema(description = "자기소개", example = "안녕하세요, 새로운 사용자입니다.")
    @NotBlank
    private String introduce;

    @ArraySchema(schema = @Schema(description = "추천 가중치 항목 3개", example = "[\"smellLevel\", \"sleepLevel\", \"tidinessLevel\"]"))
    @Size(min = 3, max = 3, message = "정확히 3개의 항목을 선택해야 합니다.")
    private List<@NotBlank String> recommendationWeights;

    public UserProfile toEntity(User user){
        return UserProfile.builder()
                .alcoholCount(alcoholCount)
                .cookingCount(cookingCount)
                .smellLevel(smellLevel)
                .dishShare(dishShare)
                .sleepLevel(sleepLevel)
                .sleepHabit(sleepHabit)
                .phoneMode(phoneMode)
                .earphoneUsage(earphoneUsage)
                .bathroomCleaningLevel(bathroomCleaningLevel)
                .tidinessLevel(tidinessLevel)
                .smoking(smoking)
                .pet(pet)
                .disease(disease)
                .user(user)
                .introduce(introduce)
                .recommendationWeights(recommendationWeights)
                .build();
    }
}
