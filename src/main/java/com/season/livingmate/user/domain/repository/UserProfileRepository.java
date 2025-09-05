package com.season.livingmate.user.domain.repository;

import com.season.livingmate.user.domain.User;
import com.season.livingmate.user.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(Long userId);
    boolean existsByUser(User user);

    Optional<UserProfile> findByUser(User user);
}
