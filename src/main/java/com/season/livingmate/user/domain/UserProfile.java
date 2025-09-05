package com.season.livingmate.user.domain;

import com.season.livingmate.global.converter.StringListJsonConverter;
import com.season.livingmate.global.entity.BaseEntity;
import com.season.livingmate.user.api.dto.resquest.UserProfileUpdateReqDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

// 나중에 json 검증 코드 작성하기!!
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkType workType; // 통근 유형

    @Column(columnDefinition = "json", nullable = false)
    @Convert(converter = StringListJsonConverter.class)
    private List<String> workDays;

    private String wakeUpTimeWorkday; // 출근일 기상 시간
    private String goWorkTime; // 출근 시간
    private String comeHomeTime; // 귀가 시간
    private String sleepTimeWorkday; // 출근 취짐 시간
    private String wakeUpTimeHoliday; // 휴일 기상 시간
    private String sleepTimeHoliday; // 휴일 취침 시간

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlarmCount alarmCount;

    /*
    * 식사 습관
    * */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CountRange cookingCount; // 요리 빈도

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SensitivityLevel smellLevel; // 냄새 민감도


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CountRange alcoholCount; // 음주 횟수

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DishShare dishShare; // 식기류 공유 여부

    /*
    * 청소 습관
    * */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SensitivityLevel bathroomCleaningLevel; // 화장실 청소 빈도

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SensitivityLevel tidinessLevel; // 정리정돈 성향

    /*
    * 소리 민감도
    * */

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SensitivityLevel sleepLevel; // 잠귀 민감도

    @Column(columnDefinition = "json" ,nullable = false)
    private SleepHabit sleepHabit; // 잠버릇

    @Column(columnDefinition = "json", nullable = false)
    private PhoneMode phoneMode; // 나의 휴대폰 모드

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EarphoneUsage earphoneUsage; // 나의 이어폰 사용 형대


    /*
    * 기타 생활 습관
    * */
    @Column(nullable = false)
    private boolean smoking; // 흡연 여부

    @Column(columnDefinition = "json", nullable = false)
    @Convert(converter = StringListJsonConverter.class)
    private List<String> pet;


    // 질병
    @Column(nullable = true)
    private String disease;


    // 자기소개
    @Column(length = 300)
    @Size(min = 10, max = 300)
    private String introduce;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public void update(UserProfileUpdateReqDto dto) {
        if (dto.getWorkType() != null) this.workType = dto.getWorkType();
        if (dto.getWorkDays() != null && !dto.getWorkDays().isEmpty()) this.workDays = dto.getWorkDays();
        if (dto.getWakeUpTimeWorkday() != null) this.wakeUpTimeWorkday = dto.getWakeUpTimeWorkday();
        if (dto.getGoWorkTime() != null) this.goWorkTime = dto.getGoWorkTime();
        if (dto.getComeHomeTime() != null) this.comeHomeTime = dto.getComeHomeTime();
        if (dto.getSleepTimeWorkday() != null) this.sleepTimeWorkday = dto.getSleepTimeWorkday();
        if (dto.getWakeUpTimeHoliday() != null) this.wakeUpTimeHoliday = dto.getWakeUpTimeHoliday();
        if (dto.getSleepTimeHoliday() != null) this.sleepTimeHoliday = dto.getSleepTimeHoliday();
        if (dto.getAlarmCount() != null) this.alarmCount = dto.getAlarmCount();
        if (dto.getAlcoholCount() != null) this.alcoholCount = dto.getAlcoholCount();
        if (dto.getCookingCount() != null) this.cookingCount = dto.getCookingCount();
        if (dto.getSmellLevel() != null) this.smellLevel = dto.getSmellLevel();
        if (dto.getDishShare() != null) this.dishShare = dto.getDishShare();
        if (dto.getSleepLevel() != null) this.sleepLevel = dto.getSleepLevel();
        if (dto.getSleepHabit() != null) this.sleepHabit = dto.getSleepHabit();
        if (dto.getPhoneMode() != null) this.phoneMode = dto.getPhoneMode();
        if (dto.getEarphoneUsage() != null) this.earphoneUsage = dto.getEarphoneUsage();
        if (dto.getBathroomCleaningLevel() != null) this.bathroomCleaningLevel = dto.getBathroomCleaningLevel();
        if (dto.getTidinessLevel() != null) this.tidinessLevel = dto.getTidinessLevel();
        this.smoking = dto.isSmoking();
        if (dto.getPet() != null && !dto.getPet().isEmpty()) this.pet = dto.getPet();
        this.disease = dto.getDisease();
        if (dto.getIntroduce() != null) this.introduce = dto.getIntroduce();
    }

}
