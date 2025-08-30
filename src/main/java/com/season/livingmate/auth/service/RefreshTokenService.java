package com.season.livingmate.auth.service;

import com.season.livingmate.auth.entity.RefreshToken;
import com.season.livingmate.auth.repository.RefreshTokenRepository;
import com.season.livingmate.exception.CustomException;
import com.season.livingmate.exception.status.ErrorStatus;
import com.season.livingmate.user.entity.User;
import com.season.livingmate.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

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