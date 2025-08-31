package com.season.livingmate.user.api.dto.resquest;

import com.season.livingmate.user.domain.*;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "유저프로필 생성 요청 DTO")
public class CreateReqDto {

    @Schema(description = "MBTI 유형", example = "INFJ", allowableValues = {"ISTJ", "ISFJ", "INFJ", "INTJ", "ISTP", "ISFP", "INFP", "INTP", "ESTP", "ESFP", "ENFP", "ENTP", "ESTJ", "ESFJ", "ENFJ", "ENTJ"})
    @NotNull
    private Mbti mbti;

    @Schema(description = "통근 유형", example = "OFFICE", allowableValues = {"OFFICE", "STUDENT", "REMOTE", "PREELANCER"})
    private WorkType workType;

    @ArraySchema(schema = @Schema(description = "출근 요일 목록", example = "월,화,수,목,금"))
    private List<String> workDays;

    @Schema(description = "출근일 기상 시간", example = "07:00")
    @NotBlank
    private String wakeUpTimeWorkday;

    @Schema(description = "출근 시간", example = "09:00")
    @NotBlank
    private String goWorkTime;

    @Schema(description = "퇴근 시간", example = "18:00")
    @NotBlank
    private String leaveWorkTime;

    @Schema(description = "출근일 취침 시간", example = "23:00")
    @NotBlank
    private String sleepTimeWorkday;

    @Schema(description = "휴일 기상 시간", example = "08:00")
    @NotBlank
    private String wakeUpTimeHoliday;

    @Schema(description = "휴일 취침 시간", example = "24:00")
    @NotBlank
    private String sleepTimeHoliday;

    @Schema(description = "알람 듣는 횟수", example = "ONE", allowableValues = {"ONCE", "TWICE", "THREE_OR_MORE"})
    @NotNull
    private AlarmCount alarmCount;

    @Schema(description = "공부 횟수 범위", example = "ZERO", allowableValues = {"ZERO", "ONT_TO_TWO", "TWO_TO_THREE", "THREE_TO_FOUR", "FOUR_TO_FIVE"})
    @NotNull
    private CountRange studyCount;

    @Schema(description = "외출 횟수 범위", example = "ZERO",allowableValues = {"ZERO", "ONT_TO_TWO", "TWO_TO_THREE", "THREE_TO_FOUR", "FOUR_TO_FIVE"})
    @NotNull
    private CountRange outingCount;

    @Schema(description = "잠귀 민감도", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    @NotNull
    private SensitivityLevel sleepLevel;

    @ArraySchema(schema = @Schema(description = "잠버릇 목록", example = "NONE"))
    private List<@NotBlank String> sleepHabit;

    @ArraySchema(schema = @Schema(description = "선호하는 소리 목록", example = "ALAWAYS_SOUND"))
    private List<@NotBlank String> preferSound;

    @Schema(description = "이어폰 사용 형태", example = "NIGHT_ONLY" , allowableValues = {"ALAWAYS", "NIGHT_ONLY", "NOT_CARE"})
    @NotNull
    private EarphoneUsage earphoneUsage;

    @Schema(description = "청소 빈도", example = "MEDIUM", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    @NotNull
    private SensitivityLevel cleaningLevel;

    @Schema(description = "화장실 청소 빈도", example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    @NotNull
    private SensitivityLevel bathroomCleaningLevel;

    @Schema(description = "정리정돈 성향", example = "LOW", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    @NotNull
    private SensitivityLevel tidinessLevel;

    @Schema(description = "흡연 여부", example = "NONE", allowableValues = {"CIGARETTE", "VAPE", "NONE"})
    @NotNull
    private Smoking smoking;

    @Schema(description = "실내 흡연 허용 정도", example = "NO", allowableValues = {"NOT_CARE", "NO", "CIGARETTE", "VAPE"  })
    @NotNull
    private IndoorSmokingPreference indoorSmokingPreference;

    @ArraySchema(schema = @Schema(description = "반려동물 종류 목록", example = "강아지, 고양이"))
    private List<@NotBlank String> pet;

    @Schema(description = "질병 정보", example = "없음")
    @NotBlank
    private String disease;

    @Schema(description = "자기소개", example = "안녕하세요, 새로운 사용자입니다.")
    @NotBlank
    private String introduce;

    public UserProfile toEntity(User user){
        return UserProfile.builder()
                .mbti(mbti)
                .workType(workType)
                .workDays(workDays)
                .wakeUpTimeWorkday(wakeUpTimeWorkday)
                .goWorkTime(goWorkTime)
                .leaveWorkTime(leaveWorkTime)
                .wakeUpTimeHoliday(wakeUpTimeHoliday)
                .sleepTimeWorkday(sleepTimeWorkday)
                .sleepTimeHoliday(sleepTimeHoliday)
                .alarmCount(alarmCount)
                .studyCount(studyCount)
                .outingCount(outingCount)
                .sleepLevel(sleepLevel)
                .sleepHabit(sleepHabit)
                .preferSound(preferSound)
                .earphoneUsage(earphoneUsage)
                .cleaningLevel(cleaningLevel)
                .bathroomCleaningLevel(bathroomCleaningLevel)
                .tidinessLevel(tidinessLevel)
                .smoking(smoking)
                .indoorSmokingPreference(indoorSmokingPreference)
                .pet(pet)
                .disease(disease)
                .user(user)
                .introduce(introduce)
                .build();
    }
}
