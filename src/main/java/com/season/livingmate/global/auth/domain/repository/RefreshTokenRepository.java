package com.season.livingmate.global.auth.domain.repository;

import com.season.livingmate.global.auth.domain.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Modifying
    @Transactional
    void deleteByUser_Id(Long userId);

    // userId로 RefreshToken 조회
    Optional<RefreshToken> findByUser_Id(Long userId);
}
