package com.season.livingmate.domain.auth.service;

import com.season.livingmate.domain.auth.dto.response.RefreshTokenRes;
import com.season.livingmate.domain.auth.entity.RefreshToken;
import com.season.livingmate.domain.auth.repository.RefreshTokenRepository;
import com.season.livingmate.domain.auth.security.JwtProvider;
import com.season.livingmate.global.exception.CustomException;
import com.season.livingmate.global.exception.status.ErrorStatus;
import com.season.livingmate.domain.user.domain.User;
import com.season.livingmate.domain.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public RefreshTokenRes refreshToken(String oldRefreshToken) {

        // 토큰 유효성 검증
        validateRefreshToken(oldRefreshToken);

        // 사용자 정보 조회
        Long userId = jwtProvider.getUserId(oldRefreshToken);

        // 기존 토큰 조회 및 일치 여부 확인
        RefreshToken existingToken = refreshTokenRepository.findByUser_Id(userId)
                .orElseThrow(() -> new CustomException(ErrorStatus.UNAUTHORIZED));

        if (!existingToken.getRefreshToken().equals(oldRefreshToken)) {
            throw new CustomException(ErrorStatus.UNAUTHORIZED);
        }

        // 토큰 rotation: 기존 토큰 삭제
        refreshTokenRepository.deleteByUser_Id(userId);

        // 새로운 토큰 발급
        String newAccessToken = jwtProvider.generateAccessToken(userId);
        String newRefreshToken = jwtProvider.generateRefreshToken(userId);

        log.info("Refresh token rotation for user {}: old={} new={}", userId, oldRefreshToken, newRefreshToken);

        // 새 refreshToken 저장
        saveRefreshToken(userId, newRefreshToken, System.currentTimeMillis() + 86400000L);

        return RefreshTokenRes.from(newAccessToken, newRefreshToken);
    }

    // 토큰 검증 로직을 별도 메서드로 분리
    private void validateRefreshToken(String refreshToken) {
        if (jwtProvider.isExpired(refreshToken)) {
            throw new CustomException(ErrorStatus.REFRESH_TOKEN_EXPIRED);
        }

        if (!jwtProvider.isRefreshToken(refreshToken)) {
            throw new CustomException(ErrorStatus.ACCESS_TOKEN_EXPIRED);
        }
    }


    public void saveRefreshToken(Long userId, String refreshToken, Long expiredMs) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorStatus.USER_NOT_FOUND));

        // 기존 토큰이 있으면 삭제
        refreshTokenRepository.findByUser_Id(userId)
                .ifPresent(existingToken -> refreshTokenRepository.delete(existingToken));

        RefreshToken token = RefreshToken.builder()
                .user(user)
                .refreshToken(refreshToken)
                .expiredAt(expiredMs)
                .build();

        refreshTokenRepository.save(token);
    }
}