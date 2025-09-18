package com.season.livingmate.user.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.season.livingmate.user.domain.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class UserProfilePredicate {

    private static final QUserProfile up = QUserProfile.userProfile;

    // 흡연 여부에 따른 필터링
    public static BooleanExpression smokingAllowed(Boolean smoking) {
        return smoking == null ? null : up.smoking.eq(smoking);
    }

    // 반려동물
    public static BooleanExpression petAllowed(List<String> pets) {
        return (pets == null || pets.isEmpty()) ? null : up.pet.any().in(pets);
    }

    // 주 음주 횟수 (여러 범위 OR)
    public static BooleanExpression drinkingFrequency(List<CountRange> ranges) {
       return (ranges == null || ranges.isEmpty()) ? null : up.alcoholCount.in(ranges);
    }

    // 청결 성향
    public static BooleanExpression cleanlinessLevel(List<TidinessLevel> levels) {
        return (levels == null || levels.isEmpty()) ? null : up.tidinessLevel.in(levels);
    }

    // 잠귀 민감도
    public static BooleanExpression lightSleepLevel(List<SensitivityLevel> levels) {
        return (levels == null || levels.isEmpty()) ? null : up.sleepLevel.in(levels);
    }

    // 성별 제외
    public static BooleanExpression genderEqual(Gender gender) {
        return gender == null ? null : up.user.gender.eq(gender);
    }

    // 본인 제외
    public static BooleanExpression excludeUser(Long userId) {
        return userId == null ? null : up.user.id.ne(userId);
    }
}
