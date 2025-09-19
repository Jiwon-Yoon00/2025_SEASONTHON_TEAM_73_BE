package com.season.livingmate.user.domain.repository;

import com.season.livingmate.user.domain.UserProfileLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileLikeRepository extends JpaRepository<UserProfileLike,Long> {
    Page<UserProfileLike> findAllByUserId(Long userId, Pageable pageable);

    Optional<UserProfileLike> findByUserIdAndLikedUserId(Long userId, Long likedUserId);

    boolean existsByUserIdAndLikedUserId(Long id, Long targetUserId);
}
