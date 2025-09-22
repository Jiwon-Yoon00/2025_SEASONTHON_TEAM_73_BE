package com.season.livingmate.domain.auth.repository;

import com.season.livingmate.domain.auth.entity.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Long> {
    boolean existsByBlacklistToken(String token); // 블랙리스트 여부 체크

    Optional<BlacklistToken> findByBlacklistToken(String token);
}
