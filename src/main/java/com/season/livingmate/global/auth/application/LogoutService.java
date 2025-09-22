package com.season.livingmate.global.auth.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.season.livingmate.domain.auth.entity.BlacklistReason;
import com.season.livingmate.global.auth.domain.repository.RefreshTokenRepository;
import com.season.livingmate.global.auth.security.JwtProvider;
import com.season.livingmate.global.exception.Response;
import com.season.livingmate.global.exception.status.ErrorStatus;
import com.season.livingmate.global.exception.status.SuccessStatus;
import com.season.livingmate.domain.user.domain.User;
import com.season.livingmate.domain.user.domain.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistService blacklistService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public void logout(String accessToken, HttpServletResponse response) throws IOException {
        if (accessToken == null || accessToken.isEmpty()) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorStatus.INVALID_ACCESS_TOKEN);
            return;
        }

        try {
            if (!jwtProvider.isAccessToken(accessToken)) {
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorStatus.INVALID_ACCESS_TOKEN);
                return;
            }

            // 사용자 조회
            Long userId = jwtProvider.getUserId(accessToken);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Access Token → 블랙리스트 등록
            long remainingMillis = getRemainingMillis(accessToken);
            blacklistService.addToBlacklist(accessToken, BlacklistReason.LOGOUT, remainingMillis, user);

            // Refresh Token → DB에서 삭제
            refreshTokenRepository.deleteByUser_Id(userId);

            sendSuccessResponse(response, SuccessStatus.SUCCESS, null);

        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, ErrorStatus.ACCESS_TOKEN_EXPIRED);
        } catch (Exception e) {
            log.error("Logout error", e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean isBlacklisted(String token) {
        return blacklistService.isBlacklisted(token);
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private long getRemainingMillis(String token) {
        Date expiration = jwtProvider.getClaims(token).getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }

    private void sendErrorResponse(HttpServletResponse response, int status, ErrorStatus errorStatus) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        objectMapper.writeValue(response.getWriter(), Response.fail(errorStatus));
    }

    private void sendSuccessResponse(HttpServletResponse response, SuccessStatus status, Object data) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getWriter(), Response.success(status, data));
    }
}
