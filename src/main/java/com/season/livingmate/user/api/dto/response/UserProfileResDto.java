package com.season.livingmate.user.api.dto.response;

import com.season.livingmate.post.api.dto.res.PostListRes;
import com.season.livingmate.post.domain.Post;
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
    private WorkType isCertified;

    private LifeHabitDto lifeHabit;
    private MealHabitDto mealHabit;
    private EtcDto etc;
    private CleaningHabitDto cleaningHabit;
    private SoundSensitivityDto soundSensitivity;
    private String introduce;
    
    // 가중치 추가
    private List<String> recommendationWeights;
    private List<String> pet;
    private String disease;
    private String userProfileImage;
    private List<PostListRes> posts;


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
        private TidinessLevel tidinessLevel;
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

    public static UserProfileResDto from(UserProfile profile, List<Post> posts) {
        LifeHabitDto lifeHabit = new LifeHabitDto(
                profile.getWorkType() != null ? profile.getWorkType() : WorkType.UNKNOWN,
                profile.getWorkDays() != null ? profile.getWorkDays() : List.of("미작성"),
                profile.getWakeUpTimeWorkday() != null ? profile.getWakeUpTimeWorkday() : "미작성",
                profile.getGoWorkTime() != null ? profile.getGoWorkTime() : "미작성",
                profile.getComeHomeTime() != null ? profile.getComeHomeTime() : "미작성",
                profile.getSleepTimeWorkday() != null ? profile.getSleepTimeWorkday() : "미작성",
                profile.getWakeUpTimeHoliday() != null ? profile.getWakeUpTimeHoliday() : "미작성",
                profile.getSleepTimeHoliday() != null ? profile.getSleepTimeHoliday() : "미작성",
                profile.getAlarmCount() != null ? profile.getAlarmCount() : AlarmCount.UNKNOWN
        );

        MealHabitDto mealHabit = new MealHabitDto(
                profile.getCookingCount() != null ? profile.getCookingCount() : MealWay.UNKNOWN,
                profile.getSmellLevel() != null ? profile.getSmellLevel() : SensitivityLevel.UNKNOWN,
                profile.getAlcoholCount() != null ? profile.getAlcoholCount() : CountRange.UNKNOWN,
                profile.getDishShare() != null ? profile.getDishShare() : DishShare.UNKNOWN
        );

        CleaningHabitDto cleaningHabit = new CleaningHabitDto(
                profile.getBathroomCleaningLevel() != null ? profile.getBathroomCleaningLevel() : SensitivityLevel.UNKNOWN,
                profile.getTidinessLevel() != null ? profile.getTidinessLevel() : TidinessLevel.UNKNOWN
        );

        SoundSensitivityDto soundSensitivity = new SoundSensitivityDto(
                profile.getSleepLevel() != null ? profile.getSleepLevel() : SensitivityLevel.UNKNOWN,
                profile.getSleepHabit() != null ? profile.getSleepHabit() : List.of("미작성"),
                profile.getPhoneMode() != null ? profile.getPhoneMode() : PhoneMode.UNKNOWN,
                profile.getEarphoneUsage() != null ? profile.getEarphoneUsage() : EarphoneUsage.UNKNOWN
        );

        EtcDto etc = new EtcDto(
                profile.isSmoking() ? "흡연" : "비흡연",
                profile.getPet() != null ? profile.getPet() : List.of("미작성")
        );

        List<PostListRes> postList = (posts == null || posts.isEmpty())
                ? List.of()
                : posts.stream().map(PostListRes::from).toList();

        return new UserProfileResDto(
                profile.getUser().getId(),
                profile.getUser().getNickname(),
                profile.getUser().getAge(),
                profile.getUser().getGender(),
                profile.getUser().getWorkType(),
                lifeHabit,
                mealHabit,
                etc,
                cleaningHabit,
                soundSensitivity,
                profile.getIntroduce() != null ? profile.getIntroduce() : "미작성",
                profile.getRecommendationWeights() != null ? profile.getRecommendationWeights() : List.of("미작성"),
                profile.getPet() != null ? profile.getPet() : List.of("미작성"),
                profile.getDisease() != null ? profile.getDisease() : "미작성",
                profile.getProfileImageUrl() != null ? profile.getProfileImageUrl() : "미작성",
                postList
        );

    }
}


