package com.season.livingmate.domain.post.domain.repository;

import com.season.livingmate.domain.post.api.dto.req.PostSearchReq;
import com.season.livingmate.domain.post.domain.Post;
import com.season.livingmate.domain.post.domain.RoomType;
import com.season.livingmate.domain.user.api.dto.resquest.UserFilterReq;
import com.season.livingmate.domain.user.domain.entity.Gender;
import com.season.livingmate.domain.user.domain.entity.User;
import com.season.livingmate.domain.user.domain.UserProfile;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class PostSpecs {

    // 제목, 본문 키워드 검색
    public static Specification<Post> keyword(String q) {
        if(q == null || q.isBlank()) return null;
        final String like = "%" + q.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("title")), like),
                cb.like(cb.lower(root.get("content")), like)
        );
    }

    // 보증금 (둘 다 비었을 때만 무시)
    public static Specification<Post> depositBetween(Integer min, Integer max) {
        if(min == null && max == null) return null;

        return (root, query, cb) -> {
            if(min != null && max != null)
                return cb.between(root.get("deposit"), min, max);
            else if(min != null)
                return cb.greaterThanOrEqualTo(root.get("deposit"), min);
            else
                return cb.lessThanOrEqualTo(root.get("deposit"), max);
        };
    }

    // 월세 + 관리비
    public static Specification<Post> totalMonthlyCostBetween(Integer min, Integer max) {
        if(min == null && max == null) return null;

        return (root, query, cb) -> {

            Expression<Integer> monthlyRent = root.get("monthlyRent");
            Expression<Integer> maintenanceFee = root.get("maintenanceFee");

            Expression<Integer> totalCost = cb.sum(monthlyRent, maintenanceFee);

            if (min != null && max != null)
                return cb.between(totalCost, cb.literal(min), cb.literal(max));
            else if (min != null)
                return cb.greaterThanOrEqualTo(totalCost, cb.literal(min));
            else
                return cb.lessThanOrEqualTo(totalCost, cb.literal(max));
        };
    }

    // 방형태 (enum으로 받음)
    public static Specification<Post> roomTypes(Collection<RoomType> roomTypes) {
        if(roomTypes == null || roomTypes.isEmpty()) return null;
        return (root, query, cb) -> root.get("roomType").in(roomTypes);
    }

    // 주소
    public static Specification<Post> locationContainsDong(Collection<String> dongs) {
        if (dongs == null || dongs.isEmpty()) return null;

        List<String> normalized = dongs.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .toList();

        if(normalized.isEmpty()) return null;

        return (root, query, cb) -> {
            var loc = cb.lower(root.get("location"));
            List<Predicate> ors = normalized.stream()
                    .map(d -> "%" + d + "%")
                    .map(pattern -> cb.like(loc, pattern))
                    .toList();
            return cb.or(ors.toArray(new Predicate[0]));
        };
    }

    public static Specification<Post> build(PostSearchReq req, Gender userGender) {
        return build(
                req.keyword(),
                req.minDeposit(),
                req.maxDeposit(),
                req.minMonthlyCost(),
                req.maxMonthlyCost(),
                req.getRoomTypeEnums(), // 한국어를 enum으로 변환
                req.dongs(),
                userGender
        );
    }

    // 입력된 조건만 AND
    public static Specification<Post> build(
            String keyword,
            Integer minDeposit,
            Integer maxDeposit,
            Integer minMonthlyCost,
            Integer maxMonthlyCost,
            Collection<RoomType> roomTypes, // enum 리스트
            Collection<String> dongs,
            Gender userGender
    ) {
        List<Specification<Post>> parts = new ArrayList<>();
        parts.add(keyword(keyword));
        parts.add(depositBetween(minDeposit, maxDeposit));
        parts.add(totalMonthlyCostBetween(minMonthlyCost, maxMonthlyCost));
        parts.add(roomTypes(roomTypes));
        parts.add(locationContainsDong(dongs));
        parts.add(matchUserGender(userGender));

        parts.removeIf(Objects::isNull);

        return parts.isEmpty()
                ? (root, q, cb) -> cb.conjunction()   // 전체 조회
                : Specification.allOf(parts);
    }

    // 성별 매칭 필터링
    public static Specification<Post> matchUserGender(Gender userGender) {
        if (userGender == null) return null;

        return (root, query, cb) -> {
            // 게시글의 선호 성별 필드
            var preferredGenderField = root.<String>get("preferredGender");

            // 사용자 성별이 게시글의 선호 성별에 포함되는지 확인
            return cb.or(
                    // 선호 성별이 없으면 모두 출력
                    cb.or(
                            cb.isNull(preferredGenderField),
                            cb.equal(preferredGenderField, "")
                    ),
                    // 선호 성별에 사용자 성별이 포함된 경우
                    cb.like(preferredGenderField, "%" + userGender.name() + "%"),
                    // 선호 성별에 "MALE,FEMALE" 같은 형태로 포함된 경우
                    cb.like(preferredGenderField, userGender.name() + ",%"),
                    cb.like(preferredGenderField, "%," + userGender.name()),
                    cb.like(preferredGenderField, "%," + userGender.name() + "%")
            );
        };
    }

    public static Specification<Post> withUserFilter(UserFilterReq filter) {
        if (filter == null) {
            return null;
        }

        return (root, query, cb) -> {
            Join<Post, User> userJoin = root.join("user");                // Post → User
            Join<User, UserProfile> profileJoin = userJoin.join("userProfile"); // User → UserProfile

            List<Predicate> predicates = new ArrayList<>();

            // 흡연 여부
            if (filter.getSmoking() != null) {
                predicates.add(cb.equal(profileJoin.get("smoking"), filter.getSmoking()));
            }

            // 음주 횟수
            if (filter.getAlcoholCount() != null && !filter.getAlcoholCount().isEmpty()) {
                predicates.add(profileJoin.get("alcoholCount").in(filter.getAlcoholCount()));
            }

            // 잠귀 민감도
            if (filter.getSleepLevel() != null && !filter.getSleepLevel().isEmpty()) {
                predicates.add(profileJoin.get("sleepLevel").in(filter.getSleepLevel()));
            }

            // 반려동물 (LIKE 검색, 리스트 OR)
            if (filter.getPet() != null && !filter.getPet().isEmpty()) {
                List<Predicate> petPredicates = filter.getPet().stream()
                        .map(p -> cb.like(profileJoin.get("pet"), "%" + p + "%"))
                        .toList();
                predicates.add(cb.or(petPredicates.toArray(new Predicate[0])));
            }

            // 정리정돈 성향
            if (filter.getTidinessLevel() != null && !filter.getTidinessLevel().isEmpty()) {
                predicates.add(profileJoin.get("tidinessLevel").in(filter.getTidinessLevel()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}

