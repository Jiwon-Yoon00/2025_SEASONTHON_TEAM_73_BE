package com.season.livingmate.chat.infra;

import com.season.livingmate.auth.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if(request instanceof ServletServerHttpRequest servletRequest){
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();

            String header  = httpServletRequest.getHeader("Authorization");
            //Authorization 헤더 검증
            if (header == null || !header.startsWith("Bearer ")) {
                log.debug("Authorization header missing or invalid: {}", header);
            }

            String token = header.replace("Bearer ", "");

            if (token != null && jwtProvider.isAccessToken(token) && !jwtProvider.isExpired(token)) {
                Long userId = jwtProvider.getUserId(token);
                attributes.put("userId", userId); // WebSocket 세션에 userId 저장
                log.info("WebSocket 연결 준비 - userId: {}", userId);
                return true;
            } else {
                log.warn("WebSocket 연결 거부 - JWT 없음 또는 만료됨");
                return false; // 인증 실패 → 연결 거부
            }
        }

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
