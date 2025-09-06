package com.season.livingmate.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.season.livingmate.exception.CustomException;
import com.season.livingmate.exception.Response;
import com.season.livingmate.exception.status.ErrorStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final CustomUserDetailService customUserDetailService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (header == null || !header.startsWith("Bearer ")) {
            log.debug("Authorization header missing or invalid: {}", header);
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");
        log.info("Token received: {}", token);

        if (!request.getRequestURI().equals("/auth/refresh")) {
            setAuthentication(token);
        }

        filterChain.doFilter(request, response);
    }

    // 토큰 검증/ 유형 체크 -> Refresh 인지 access인지
    private boolean isValidToken(HttpServletRequest request, String token, HttpServletResponse response) throws IOException {
        try {
            if (request.getRequestURI().equals("/auth/refresh")) {
                if (!jwtProvider.isRefreshToken(token)) {
                    sendErrorResponse(response, ErrorStatus.BAD_REQUEST);
                    return false;
                }
                return true; // Refresh Token이면 통과
            }

            if (jwtProvider.isExpired(token)) {
                sendErrorResponse(response, ErrorStatus.ACCESS_TOKEN_EXPIRED);
                return false;
            }

            if (!jwtProvider.isAccessToken(token)) {
                sendErrorResponse(response, ErrorStatus.INVALID_ACCESS_TOKEN);
                return false;
            }

            return true;
        } catch (CustomException e) {
            sendErrorResponse(response, e.getErrorCode());
            return false;
        }
    }

    private void setAuthentication(String token) {
        Long userId = jwtProvider.getUserId(token);
        UserDetails userDetails = customUserDetailService.loadUserById(userId);

        Authentication authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private void sendErrorResponse(HttpServletResponse response, ErrorStatus errorStatus) throws IOException {
        response.setStatus(errorStatus.getStatus().value());
        response.setContentType("application/json; charset=UTF-8");
        Response<?> errorResponse = Response.fail(errorStatus);
        String json = objectMapper.writeValueAsString(errorResponse);
        PrintWriter writer = response.getWriter();
        writer.print(json);
        writer.flush();
    }

}
