package com.season.livingmate.global.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.season.livingmate.global.auth.application.LogoutService;
import com.season.livingmate.global.exception.Response;
import com.season.livingmate.global.exception.status.ErrorStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class CustomLogoutFilter extends OncePerRequestFilter {

    private final LogoutService logoutService; // 모든 비즈니스 로직 위임
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = logoutService.resolveAccessToken(request);

        // 블랙리스트 체크
        if (accessToken != null && logoutService.isBlacklisted(accessToken)) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ErrorStatus.UNAUTHORIZED);
            return;
        }

        // 로그아웃 요청 처리
        if ("/auth/logout".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
            logoutService.logout(accessToken, response);
            return;
        }

        // 그 외 요청은 다음 필터로
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, ErrorStatus errorStatus) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        objectMapper.writeValue(response.getWriter(), Response.fail(errorStatus));
    }
}