package com.season.livingmate.user.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.season.livingmate.user.api.dto.resquest.UserFilterReqDto;
import com.season.livingmate.user.domain.Gender;
import com.season.livingmate.user.domain.QUser;
import com.season.livingmate.user.domain.QUserProfile;
import com.season.livingmate.user.domain.UserProfile;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserProfileRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    public UserProfileRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Page<UserProfile> filterWithBoostFirst(UserFilterReqDto dto,
                                                  Gender gender,
                                                  Long loggedInUserId,
                                                  List<Long> boostUserIds,
                                                  Pageable pageable) {
        QUserProfile up = QUserProfile.userProfile;
        QUser u = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();
        if (dto != null) {
            if(dto.getPet() != null) {
                builder.and(UserProfilePredicate.petAllowed(dto.getPet()));
            }

            if(dto.getTidinessLevel() != null){
                builder.and(UserProfilePredicate.cleanlinessLevel(dto.getTidinessLevel()));
            }

            if(dto.getSleepLevel() != null){
                builder.and(UserProfilePredicate.lightSleepLevel(dto.getSleepLevel()));
            }

            if(dto.getAlcoholCount() != null){
                builder.and(UserProfilePredicate.drinkingFrequency(dto.getAlcoholCount()));
            }

            builder.and(UserProfilePredicate.smokingAllowed(dto.isSmoking()));
            builder.and(UserProfilePredicate.genderEqual(gender));
        }
        builder.and(UserProfilePredicate.excludeUser(loggedInUserId));

        // 메인 쿼리
        JPAQuery<UserProfile> query = queryFactory
                .selectFrom(up)
                .leftJoin(up.user, u).fetchJoin()
                .where(builder)
                .orderBy(
                        new CaseBuilder()
                                .when(u.id.in(boostUserIds)).then(0)
                                .otherwise(1).asc(),
                        up.createdAt.desc()
                );

        // 페이징
        long total = query.fetch().size();
        List<UserProfile> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }
}
