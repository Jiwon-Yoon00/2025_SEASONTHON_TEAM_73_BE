package com.season.livingmate.user.domain.repository;

import com.season.livingmate.user.domain.Gender;
import com.season.livingmate.user.domain.UserBoost;
import com.season.livingmate.user.domain.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBoostRepository  extends JpaRepository<UserBoost, Long> {

    @Query("SELECT ub.userId FROM UserBoost ub")
    List<Long> findBoostUserIds();
}
