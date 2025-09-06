package com.season.livingmate.user.api.dto.resquest;

import com.season.livingmate.user.domain.*;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "유저프로필 수정 요청 DTO (모든 필드 선택적)")
public class UserProfileUpdateReqDto {

    @Schema(description = "통근 유형", example = "OFFICE")
    private WorkType workType;

    @ArraySchema(schema = @Schema(description = "출근 요일 목록", example = "월,화,수,목,금"))
    private List<String> workDays;

    @Schema(description = "출근일 기상 시간", example = "07:00")
    private String wakeUpTimeWorkday;

    @Schema(description = "출근 시간", example = "09:00")
    private String goWorkTime;

    @Schema(description = "귀가 시간", example = "18:00")
    private String comeHomeTime;

    @Schema(description = "출근일 취침 시간", example = "23:00")
    private String sleepTimeWorkday;

    @Schema(description = "휴일 기상 시간", example = "08:00")
    private String wakeUpTimeHoliday;

    @Schema(description = "휴일 취침 시간", example = "24:00")
    private String sleepTimeHoliday;

    @Schema(description = "알람 듣는 횟수", example = "ONE")
    private AlarmCount alarmCount;

    // 식사 습관
    @Schema(description = "요리 빈도", example = "ZERO",allowableValues = {"ZERO", "ONE_TO_THREE", "MORE_THAN_FOUR"})
    private MealWay cookingCount;

    @Schema(description = "냄새 민감도", example = "LOW",allowableValues = {"LOW", "MEDIUM",  "HIGH"})
    private SensitivityLevel smellLevel;

    @Schema(description = "주 음주 횟수", example = "ZERO")
    private CountRange alcoholCount;

    @Schema(description = "식기류 공유 여부", example = "SHARE")
    private DishShare dishShare;


    // 소리 민감도
    @Schema(description = "잠귀 민감도", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    private SensitivityLevel sleepLevel;

    @ArraySchema(schema = @Schema(description = "잠버릇 목록", example = "NONE"))
    private List<String> sleepHabit;

    @Schema(description = "나의 휴대폰 모드", example = "SILENT" , allowableValues = {"VIBRATION", "SILENT", "NONE"})
    private PhoneMode phoneMode;

    @Schema(description = "나의 이어폰 사용", example = "NIGHT_ONLY" , allowableValues = {"ALAWAYS", "NIGHT_ONLY", "NOT_CARE"})
    private EarphoneUsage earphoneUsage;


    // 청소 습관
    @Schema(description = "화장실 청소 빈도", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    private SensitivityLevel bathroomCleaningLevel;

    @Schema(description = "정리정돈 성향", example = "LOW", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    private SensitivityLevel tidinessLevel;

    @Schema(description = "흡연 여부", example = "false")
    private boolean smoking;

    @ArraySchema(schema = @Schema(description = "반려동물 종류 목록", example = "강아지, 고양이"))
    private List<String> pet;

    @Schema(description = "질병 정보", example = "없음")
    private String disease;

    @Schema(description = "자기소개", example = "안녕하세요, 새로운 사용자입니다.")
    private String introduce;
}