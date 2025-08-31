package com.season.livingmate.auth.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

// JWT 토큰을 생성/검증
// 보통 access token을 만들어주고, 클라이언트가 보낸 JWT의 유효성을 확인하는 핵심 기능을 수행
@Component
@Slf4j
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshExpiration;

    private SecretKey secretKey;
    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    // 토큰 생성
    public String generateAccessToken(Long userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("userId", userId)
                .claim("type", "accessToken")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(Long userId) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("userId", userId)
                .claim("type", "refreshToken")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(secretKey)
                .compact();
    }

    // 토큰 검증
    public String getUsername(String token) {
        return jwtParser.parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    public Long getUserId(String token ) {
        return jwtParser.parseSignedClaims(token)
                .getPayload()
                .get("userId", Long.class);
    }


    public Boolean isExpired(String token) {
        return jwtParser.parseSignedClaims(token)
                .getPayload()
                .getExpiration().before(new Date());
    }

    public Boolean isAccessToken(String token) {
        try {
            String type = jwtParser.parseSignedClaims(token)
                    .getPayload()
                    .get("type", String.class);
            return "accessToken".equals(type);
        } catch (Exception e) {
            return false; // type 클레임 없음 or 파싱 오류
        }
    }

    public Boolean isRefreshToken(String token) {
        try {
            String type = jwtParser.parseSignedClaims(token)
                    .getPayload()
                    .get("type", String.class);
            return "refreshToken".equals(type);
        } catch (Exception e) {
            return false; // type 클레임 없음 or 파싱 오류
        }
    }


    public Claims getClaims(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    // ?
    // 토큰 유효성
    public boolean validateToken(String token) {
        try {
            jwtParser.parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            // 토큰이 만료되었지만 사용자가 재발급 시도 중일 수 있음
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            // 변조된 토큰
            return false;
        }
    }

}
