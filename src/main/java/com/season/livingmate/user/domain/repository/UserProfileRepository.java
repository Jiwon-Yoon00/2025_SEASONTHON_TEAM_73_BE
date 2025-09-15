package com.season.livingmate.user.domain.repository;

import com.season.livingmate.post.domain.Post;
import com.season.livingmate.user.domain.Gender;
import com.season.livingmate.user.domain.User;
import com.season.livingmate.user.domain.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>, JpaSpecificationExecutor<UserProfile> {
    Optional<UserProfile> findByUserId(Long userId);

    Optional<UserProfile> findByUser(User user);

    @Query("SELECT up FROM UserProfile up " +
            "WHERE up.user.gender = :gender AND up.user.id <> :excludeId " +
            "ORDER BY CASE WHEN up.user.id IN :boostUserIds THEN 0 ELSE 1 END, up.user.id DESC")
    Page<UserProfile> findAllWithBoostOrder(@Param("gender") Gender gender,
                                            @Param("excludeId") Long excludeId,
                                            @Param("boostUserIds") List<Long> boostUserIds,
                                            Pageable pageable);

}
