package com.season.livingmate.user.domain.repository;
import com.season.livingmate.user.api.dto.resquest.UserFilterReqDto;
import com.season.livingmate.user.domain.*;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.criteria.Predicate;


public final class UserSpec {
    // 흡연 여부에 따른 필터링
    public static Specification<UserProfile> smokingAllowed(Boolean smoking) {
        if (smoking == null) return null;
        return (root, query, cb) -> cb.equal(root.get("smoking"), smoking);
    }

    // 반려동물 여부에 따른 필터링
    public static Specification<UserProfile> petAllowed(List<String> pets) {
        if (pets == null || pets.isEmpty()) return null;
        return (root, query, cb) -> {
            var petField = root.get("pet").as(String.class);
            Predicate[] predicates = pets.stream()
                    .map(pet -> cb.like(petField, "%" + pet + "%"))
                    .toArray(Predicate[]::new);
            return cb.or(predicates);
        };
    }

    // 주 음주 횟수에 따른 필터링
    public static Specification<UserProfile> drinkingFrequency(List<CountRange> frequency) {
        if (frequency == null || frequency.isEmpty()) return null;
        return (root, query, cb) -> root.get("alcoholCount").in(frequency);
    }

    // 정리정돈 성향에 따른 필터링
    public static Specification<UserProfile> cleanlinessLevel(List<SensitivityLevel> level) {
        if (level == null) return null;
        return (root, query, cb) -> root.get("tidinessLevel").in(level);
    }

    // 잠귀 민감도에 따른 필터링
    public static Specification<UserProfile> lightSleepLevel(List<SensitivityLevel> level) {
        if (level == null || level.isEmpty()) return null;
        return (root, query, cb) -> root.get("sleepLevel").in(level);
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
              dto.getPet(),
              dto.getTidinessLevel(),
              gender,
              loggedInUserId
      );
    }

    public static Specification<UserProfile> build(
            Boolean smoking,
            List<CountRange> alcoholCount,
            List<SensitivityLevel> sleepLevel,
            List<String> pets,
            List<SensitivityLevel> tidinessLevel,
            Gender userGender,
            Long loggedInUserId
    ) {
        List<Specification<UserProfile>> parts = new ArrayList<>();
        parts.add(smokingAllowed(smoking));
        parts.add(drinkingFrequency(alcoholCount));
        parts.add(lightSleepLevel(sleepLevel));
        parts.add(petAllowed(pets));
        parts.add(cleanlinessLevel(tidinessLevel));
        parts.add(matchUserGender(userGender));
        parts.add(excludeUser(loggedInUserId));

        return parts.stream().filter(Objects::nonNull).reduce(Specification::and)
                .orElse((root, q, cb) -> cb.conjunction());

    }
}
