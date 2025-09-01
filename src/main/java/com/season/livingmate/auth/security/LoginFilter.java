package com.season.livingmate.auth.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.season.livingmate.auth.dto.request.LoginReqDto;
import com.season.livingmate.auth.service.RefreshTokenService;
import com.season.livingmate.exception.Response;
import com.season.livingmate.exception.status.ErrorStatus;
import com.season.livingmate.exception.status.SuccessStatus;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper; // json과 객체간의 변환을 담당, LocalDateTime 직렬화 문제
    private final RefreshTokenService refreshTokenService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try{
            LoginReqDto reqDto = objectMapper.readValue(request.getInputStream(), LoginReqDto.class);

            String username = reqDto.getUsername();
            String password = reqDto.getPassword();

            // 일종의 dto
            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(username, password, null);

            // token에 담은 검증을 위한 AuthenticationManager에게 넘겨줌
            return authenticationManager.authenticate(token);
        }catch(Exception e){
            throw new AuthenticationServiceException(e.getMessage());
        }
    }

    // 로그인 성공 시 실행하는 메소드 - jwt 발급
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws
            IOException {
        log.info("💡 로그인 성공");

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        Long userId = userDetails.getUserId();

        String accessToken = jwtProvider.generateAccessToken(userId);
        String refreshToken = jwtProvider.generateRefreshToken(userId);
        refreshTokenService.saveRefreshToken(userId, refreshToken, 86400000L);

        // accessToken
        response.setHeader("Authorization", "Bearer " + accessToken);

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("username", username);
        map.put("accessToken", accessToken);
        map.put("refreshToken", refreshToken);

        Response<Map<String, Object>> apiRes = Response.success(SuccessStatus.SUCCESS,map);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        objectMapper.writeValue(response.getWriter(), apiRes);
    }


    // 로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws
            IOException {
        log.info("❌ 로그인 실패");
        // 예: "이메일 또는 비밀번호가 잘못되었습니다"
        ErrorStatus errorStatus = ErrorStatus.UNAUTHORIZED; // 기본값

        Response<?> errorResponse = Response.fail(errorStatus);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401

        objectMapper.writeValue(response.getWriter(), errorResponse);

    }
}
