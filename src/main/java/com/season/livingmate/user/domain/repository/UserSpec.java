package com.season.livingmate.user.domain.repository;
import com.season.livingmate.user.api.dto.resquest.UserFilterReqDto;
import com.season.livingmate.user.domain.*;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public final class UserSpec {
    // 흡연 여부에 따른 필터링
    public static Specification<UserProfile> smokingAllowed(Boolean smoking) {
        if (smoking == null) return null;
        return (root, query, cb) -> cb.equal(root.get("smoking"), smoking);
    }

    // 반려동물 여부에 따른 필터링
    public static Specification<UserProfile> petAllowed(Boolean hasPet) {
        if (hasPet == null) return null;
        return (root, query, cb) -> hasPet
                ? cb.isNotNull(root.get("pet"))
                : cb.isNull(root.get("pet"));
    }

    // 주 음주 횟수에 따른 필터링
    public static Specification<UserProfile> drinkingFrequency(CountRange frequency) {
        if (frequency == null) return null;
        return (root, query, cb) -> cb.equal(root.get("alcoholCount"), frequency);
    }

    // 정리정돈 성향에 따른 필터링
    public static Specification<UserProfile> cleanlinessLevel(SensitivityLevel level) {
        if (level == null) return null;
        return (root, query, cb) -> cb.equal(root.get("tidinessLevel"), level);
    }

    // 잠귀 민감도에 따른 필터링
    public static Specification<UserProfile> lightSleepLevel(SensitivityLevel level) {
        if (level == null) return null;
        return (root, query, cb) -> cb.equal(root.get("sleepLevel"), level);
    }

    // 성별 매칭
    public static Specification<UserProfile> matchUserGender(Gender userGender) {
        if (userGender == null) return null;

        return (root, query, cb) -> {
            var userJoin = root.join("user");
            var genderField = userJoin.get("gender");

            return cb.equal(genderField, userGender);
        };
    }

    public static Specification<UserProfile> excludeUser(Long userId) {
        if (userId == null) return null;
        return (root, query, cb) -> cb.notEqual(root.get("user").get("id"), userId);
    }


    // 통합
    public static Specification<UserProfile> build(UserFilterReqDto dto, Gender gender, Long loggedInUserId) {
      return build(
              dto.isSmoking(),
              dto.getAlcoholCount(),
              dto.getSleepLevel(),
              dto.getPet() != null && !dto.getPet().isEmpty() ? true : null,
              dto.getTidinessLevel(),
              gender,
                loggedInUserId
      );
    }

    public static Specification<UserProfile> build(
            Boolean smoking,
            CountRange alcoholCount,
            SensitivityLevel sleepLevel,
            Boolean hasPet,
            SensitivityLevel tidinessLevel,
            Gender userGender,
            Long loggedInUserId
    ) {
        List<Specification<UserProfile>> parts = new ArrayList<>();
        parts.add(smokingAllowed(smoking));
        parts.add(drinkingFrequency(alcoholCount));
        parts.add(lightSleepLevel(sleepLevel));
        parts.add(petAllowed(hasPet));
        parts.add(cleanlinessLevel(tidinessLevel));
        parts.add(matchUserGender(userGender));
        parts.add(excludeUser(loggedInUserId));

        return parts.stream().filter(Objects::nonNull).reduce(Specification::and)
                .orElse((root, q, cb) -> cb.conjunction());

    }
}
