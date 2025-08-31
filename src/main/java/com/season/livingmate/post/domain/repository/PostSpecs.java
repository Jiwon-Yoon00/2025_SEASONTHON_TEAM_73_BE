package com.season.livingmate.post.domain.repository;

import com.season.livingmate.post.domain.Post;
import com.season.livingmate.post.domain.RoomType;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    // 보증금
    public static Specification<Post> depositBetween(Integer min, Integer max) {
        if(min == null || max == null) return null;

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
        if(min == null || max == null) return null;

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
    public static Specification<Post> postStatus(String gu, Collection<String> dongList) {
        if(gu == null || gu.isBlank() || dongList == null || dongList.isEmpty()) return null;

        final String seoulLike = "%서울%";
        final String guLike = "%" + gu.trim().toLowerCase() + "%";

        final List<String> dongLikes = dongList.stream()
                .filter(Objects::nonNull)
                .map(s -> "%" + s.trim().toLowerCase() + "%")
                .collect(Collectors.toList());

        return (root, query, cb) -> {
            Predicate pSeoul = cb.like(cb.lower(root.get("location")), seoulLike);
            Predicate pGu = cb.like(cb.lower(root.get("location")), guLike);

            List<Predicate> dongPreds = dongLikes.stream()
                    .map(like -> cb.like(cb.lower(root.get("location")), like))
                    .collect(Collectors.toList());

            Predicate pDongOr = cb.or(dongPreds.toArray(new Predicate[0]));
            return cb.and(pSeoul, pGu, pDongOr);
        };
    }
}
