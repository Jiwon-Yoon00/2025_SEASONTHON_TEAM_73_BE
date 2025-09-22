package com.season.livingmate.domain.like.domain.repository;

import com.season.livingmate.domain.like.domain.entity.UserLike;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLikeRepository extends JpaRepository<UserLike,Long> {
    Page<UserLike> findAllByUserId(Long userId, Pageable pageable);

    Optional<UserLike> findByUserIdAndLikedUserId(Long userId, Long likedUserId);

    boolean existsByUserIdAndLikedUserId(Long id, Long targetUserId);
}
