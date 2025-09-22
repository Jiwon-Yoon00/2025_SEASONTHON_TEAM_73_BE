package com.season.livingmate.global.config;

import com.season.livingmate.domain.chat.infra.JwtHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer  {

    private final WebSocketHandler webSocketHandler;
    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/ws-chat") // 웹소켓 엔드포인트
                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOriginPatterns("*");
        // .setAllowedOrigins(
        // 	"https:// livingmate.store",
        // ); // 명시된 도메인만 허용
    }
}
