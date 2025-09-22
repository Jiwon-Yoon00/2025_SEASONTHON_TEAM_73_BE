package com.season.livingmate.domain.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.season.livingmate.domain.auth.service.BlacklistService;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtBlacklistFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final BlacklistService blacklistService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        String token = resolveToken(request);

        if (token != null && jwtProvider.isAccessToken(token)) {
            boolean isBlacklisted = blacklistService.isBlacklisted(token);

            if (isBlacklisted) {
                log.info("블랙리스트 토큰 접근 시도: {}", token);

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                Response<?> apiRes = Response.fail(ErrorStatus.UNAUTHORIZED);
                objectMapper.writeValue(response.getWriter(), apiRes);
                return; // 요청 중단
            }
        }


        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
