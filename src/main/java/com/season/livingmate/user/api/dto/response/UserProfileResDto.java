package com.season.livingmate.user.api.dto.response;

import com.season.livingmate.user.domain.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "유저프로필 응답 DTO")
public class UserProfileResDto {

    private  Long userId;
    private String nickname;
    private int age;
    private Gender gender;
    private boolean isCertified;

    private LifeHabitDto lifeHabit;
    private MealHabitDto mealHabit;
    private EtcDto etc;
    private CleaningHabitDto cleaningHabit;
    private SoundSensitivityDto soundSensitivity;
    private String introduce;

    @Getter
    @AllArgsConstructor
    public static class LifeHabitDto {
        private WorkType workType;
        private List<String> workDays;
        private String wakeUpTimeWorkday;
        private String goWorkTime;
        private String comeHomeTime;
        private String sleepTimeWorkday;
        private String wakeUpTimeHoliday;
        private String sleepTimeHoliday;
        private AlarmCount alarmCount;
    }

    @Getter
    @AllArgsConstructor
    public static class MealHabitDto {
        private MealWay cookingCount;
        private SensitivityLevel smellLevel;
        private CountRange alcoholCount;
        private DishShare dishShare;

    }

    @Getter
    @AllArgsConstructor
    public static class CleaningHabitDto {
        private SensitivityLevel bathroomCleaningLevel;
        private SensitivityLevel tidinessLevel;
    }

    @Getter
    @AllArgsConstructor
    public static class SoundSensitivityDto {
        private SensitivityLevel sleepLevel;
        private List<String> sleepHabit;
        private PhoneMode phoneMode;
        private EarphoneUsage earphoneUsage;
    }

    @Getter
    @AllArgsConstructor
    public static class EtcDto {
        private String smoking;
        private List<String> pet;
    }

    private List<String> pet;

    private String disease;


    public static UserProfileResDto from(UserProfile profile) {
        LifeHabitDto lifeHabit = new LifeHabitDto(
                profile.getWorkType() != null ? WorkType.fromString(profile.getWorkType().getDescription()) : null,
                profile.getWorkDays(),
                profile.getWakeUpTimeWorkday(),
                profile.getGoWorkTime(),
                profile.getComeHomeTime(),
                profile.getSleepTimeWorkday(),
                profile.getWakeUpTimeHoliday(),
                profile.getSleepTimeHoliday(),
                profile.getAlarmCount() != null ? AlarmCount.fromString(profile.getAlarmCount().getDescription()) : null
        );

        MealHabitDto mealHabit = new MealHabitDto(
                profile.getCookingCount() != null ? MealWay.fromString(profile.getCookingCount().getDescription()) : null,
                profile.getSmellLevel() != null ? SensitivityLevel.fromString(profile.getSmellLevel().getDescription()) : null,
                profile.getAlcoholCount() != null ? CountRange.fromString(profile.getAlcoholCount().getDescription()) : null,
                profile.getDishShare() != null ? DishShare.fromString(profile.getDishShare().getDescription()) : null
        );

        CleaningHabitDto cleaningHabit = new CleaningHabitDto(
                profile.getBathroomCleaningLevel() != null ? SensitivityLevel.fromString(profile.getBathroomCleaningLevel().getDescription()) : null,
                profile.getTidinessLevel() != null ? SensitivityLevel.fromString(profile.getTidinessLevel().getDescription()) : null
        );

        SoundSensitivityDto soundSensitivity = new SoundSensitivityDto(
                profile.getSleepLevel() != null ? SensitivityLevel.fromString(profile.getSleepLevel().getDescription()) : null,
                profile.getSleepHabit() != null ? profile.getSleepHabit() : null,
                profile.getPhoneMode() != null ? PhoneMode.fromString(profile.getPhoneMode().getDescription()) : null,
                profile.getEarphoneUsage() != null ? EarphoneUsage.fromString(profile.getEarphoneUsage().getDescription()) : null
        );

        EtcDto etc = new EtcDto(
                profile.isSmoking() ? "흡연" : "비흡연",
                profile.getPet()
        );

        return new UserProfileResDto(
                profile.getUser().getId(),
                profile.getUser().getNickname(),
                profile.getUser().getAge(),
                profile.getUser().getGender(),
                profile.getUser().isCertified(),
                lifeHabit,
                mealHabit,
                etc,
                cleaningHabit,
                soundSensitivity,
                profile.getIntroduce(),
                profile.getPet(),
                profile.getDisease()
        );

    }
}
