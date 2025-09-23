package com.season.livingmate.domain.userProfile.domain.repository;


import com.season.livingmate.domain.user.domain.entity.User;
import com.season.livingmate.domain.userProfile.domain.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>{
    Optional<UserProfile> findByUserId(Long userId);
    Optional<UserProfile> findByUser(User user);
}
