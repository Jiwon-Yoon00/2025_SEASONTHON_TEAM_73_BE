package com.season.livingmate.user.domain.repository;

import com.season.livingmate.post.domain.Post;
import com.season.livingmate.user.domain.User;
import com.season.livingmate.user.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>, JpaSpecificationExecutor<UserProfile> {
    Optional<UserProfile> findByUserId(Long userId);

    Optional<UserProfile> findByUser(User user);
}
