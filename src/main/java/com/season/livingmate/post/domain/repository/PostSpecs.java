package com.season.livingmate.post.domain.repository;

import com.season.livingmate.post.api.dto.req.PostSearchReq;
import com.season.livingmate.post.domain.Post;
import com.season.livingmate.post.domain.RoomType;
import jakarta.persistence.criteria.Expression;
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

    // 방형태
    public static Specification<Post> roomTypes(Collection<RoomType> types) {
        if(types == null || types.isEmpty()) return null;
        return (root, query, cb) -> root.get("roomType").in(types);
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


    public static Specification<Post> build(PostSearchReq req) {
        return build(
                req.keyword(),
                req.minDeposit(),
                req.maxDeposit(),
                req.minMonthlyCost(),
                req.maxMonthlyCost(),
                req.roomTypes(),
                req.dongs()
        );
    }

    // 입력된 조건만 AND
    public static Specification<Post> build(
            String keyword,
            Integer minDeposit,
            Integer maxDeposit,
            Integer minMonthlyCost,
            Integer maxMonthlyCost,
            Collection<RoomType> roomTypes,
            Collection<String> dongs
    ) {
        List<Specification<Post>> parts = new ArrayList<>();
        parts.add(keyword(keyword));
        parts.add(depositBetween(minDeposit, maxDeposit));
        parts.add(totalMonthlyCostBetween(minMonthlyCost, maxMonthlyCost));
        parts.add(roomTypes(roomTypes));
        parts.add(locationContainsDong(dongs));

        parts.removeIf(Objects::isNull);

        return parts.isEmpty()
                ? (root, q, cb) -> cb.conjunction()   // 전체 조회
                : Specification.allOf(parts);
    }
}
