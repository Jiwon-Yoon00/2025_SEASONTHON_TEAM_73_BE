package com.season.livingmate.domain.user.domain.repository;

import com.season.livingmate.domain.user.domain.UserBoost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBoostRepository  extends JpaRepository<UserBoost, Long> {

    @Query("SELECT ub.userId FROM UserBoost ub")
    List<Long> findBoostUserIds();
}
