package com.season.livingmate.global.auth.application;


import com.season.livingmate.global.auth.domain.BlacklistReason;
import com.season.livingmate.global.auth.domain.BlacklistToken;
import com.season.livingmate.global.auth.domain.repository.BlacklistTokenRepository;
import com.season.livingmate.domain.user.domain.User;
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
