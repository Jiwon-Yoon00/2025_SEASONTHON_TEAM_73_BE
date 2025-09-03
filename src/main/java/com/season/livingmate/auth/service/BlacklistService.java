package com.season.livingmate.auth.service;


import com.season.livingmate.auth.entity.BlacklistReason;
import com.season.livingmate.auth.entity.BlacklistToken;
import com.season.livingmate.auth.repository.BlacklistTokenRepository;
import com.season.livingmate.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BlacklistService {
    private final BlacklistTokenRepository blacklistTokenRepository;

    // 블랙리스트 등록
    public void addToBlacklist(String accessToken, BlacklistReason reason, Long remainingMillis, User user) {
        BlacklistToken token = BlacklistToken.builder()
                .blacklistToken(accessToken)
                .reason(reason)
                .expiredAt(System.currentTimeMillis() + remainingMillis)
                .user(user)
                .build();

        blacklistTokenRepository.save(token);
    }

    // 블랙리스트 여부 확인
    public boolean isBlacklisted(String accessToken) {
        return blacklistTokenRepository.existsByBlacklistToken(accessToken);
    }

    // 사유 조회
    public Optional<BlacklistToken> getBlacklistInfo(String token) {
        return blacklistTokenRepository.findByBlacklistToken(token);
    }

}
