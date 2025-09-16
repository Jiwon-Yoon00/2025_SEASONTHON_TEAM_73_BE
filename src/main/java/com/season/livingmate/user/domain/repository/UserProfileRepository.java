package com.season.livingmate.user.domain.repository;

import com.season.livingmate.post.domain.Post;
import com.season.livingmate.user.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>{
    Optional<UserProfile> findByUserId(Long userId);
    Optional<UserProfile> findByUser(User user);
}
