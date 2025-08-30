package com.season.livingmate.auth.service;

import com.season.livingmate.auth.dto.response.RefreshTokenResDto;
import com.season.livingmate.auth.entity.RefreshToken;
import com.season.livingmate.auth.repository.RefreshTokenRepository;
import com.season.livingmate.auth.security.JwtProvider;
import com.season.livingmate.exception.CustomException;
import com.season.livingmate.exception.status.ErrorStatus;
import com.season.livingmate.user.entity.User;
import com.season.livingmate.user.repository.UserRepository;
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
    public RefreshTokenResDto refreshToken(String oldRefreshToken) {

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

        return RefreshTokenResDto.from(newAccessToken, newRefreshToken);
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

        RefreshToken token = RefreshToken.builder()
                .user(user)
                .refreshToken(refreshToken)
                .expiredAt(expiredMs)
                .build();

        refreshTokenRepository.save(token);
    }
}