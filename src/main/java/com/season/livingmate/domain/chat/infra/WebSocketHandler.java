package com.season.livingmate.domain.chat.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.season.livingmate.global.auth.security.CustomUserDetailService;
import com.season.livingmate.global.auth.security.CustomUserDetails;
import com.season.livingmate.domain.chat.api.dto.request.ChatMessageReq;
import com.season.livingmate.domain.chat.api.dto.response.ChatMessageRes;
import com.season.livingmate.domain.chat.application.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    private final CustomUserDetailService customUserDetailService;
    private final ObjectMapper mapper;

    private final Map<Long, Set<WebSocketSession>> chatRooms = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Long userId = (Long) session.getAttributes().get("userId");
        if (userId == null) {
            log.warn("WebSocket 연결 실패: 인증 정보 없음");
            session.sendMessage(new TextMessage(mapper.writeValueAsString(
                    Map.of("event", "error", "message", "인증되지 않은 사용자입니다.")
            )));
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("인증 정보 없음"));
            return;
        }

        log.info("WebSocket 연결 성공: {}, userId={}", session.getId(), userId);
        session.sendMessage(new TextMessage(mapper.writeValueAsString(
                Map.of("event", "connect",
                        "message", "WebSocket 연결 완료",
                        "userId", userId)
        )));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {

        Long userId = (Long) session.getAttributes().get("userId");
        if (userId == null) {
            log.warn("인증되지 않은 사용자의 메시지 요청");
            session.sendMessage(new TextMessage(mapper.writeValueAsString(
                    Map.of("event", "error", "message", "인증되지 않은 사용자입니다.")
            )));
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("인증 정보 없음"));
            return;
        }

        try{
            ChatMessageReq dto = mapper.readValue(textMessage.getPayload(), ChatMessageReq.class);
            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailService.loadUserById(userId);

            switch(dto.getWebsocket()){
                case JOIN -> handleJoin(session, dto.getChatRoomId(), userId);
                case SEND -> handleSend(session, dto, userDetails);
                case READ -> handleRead(dto.getChatRoomId(), userId);
            }
        }catch (Exception e){
            log.error("메시지 처리 중 오류", e);
            sendMessageSafe(session, e.getMessage());
        }
    }

    private void handleJoin(WebSocketSession session, Long roomId, Long userId) throws Exception {
        chatRooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
        log.info("방 입장: roomId={}, userId={}", roomId, userId);
        session.sendMessage(new TextMessage(mapper.writeValueAsString(
                Map.of("event", "joined", "roomId", roomId)
        )));

        handleRead(roomId, userId);
    }

    private void handleSend(WebSocketSession session, ChatMessageReq dto, CustomUserDetails user) throws Exception {
        ChatMessageRes resDto = chatService.createMessage(dto, user);

        // 본인에게 전송 확인
        session.sendMessage(new TextMessage(mapper.writeValueAsString(
                Map.of("event", "send",
                        "roomId", dto.getChatRoomId(),
                        "messageId", resDto.getMessageId(),
                        "content", resDto.getContent(),
                        "sender", resDto.getSenderId(),
                        "senderName", resDto.getSenderName(),
                        "senderProfile", resDto.getSenderProfile(),
                        "status", "sent")
        )));

        // 상대방에게 전달
        broadcastToOther(dto.getChatRoomId(), session, mapper.writeValueAsString(
                Map.of(
                        "event", "receive",
                        "roomId", dto.getChatRoomId(),
                        "content", resDto.getContent(),
                        "sender", resDto.getSenderId(),
                        "senderName", resDto.getSenderName(),
                        "senderProfile", resDto.getSenderProfile(),
                        "messageId", resDto.getMessageId()
                )
        ));
        log.info("메시지 전송 완료: roomId={}, messageId={}", dto.getChatRoomId(), resDto.getMessageId());
    }

    private void handleRead(Long roomId, Long userId) throws Exception {
        chatService.markMessageAsRead(roomId, userId);
        broadcast(roomId, mapper.writeValueAsString(
                Map.of("event", "read", "roomId", roomId, "readerId", userId)
        ));
        log.info("메시지 읽음 처리: roomId={}, readerId={}", roomId, userId);
    }

    // 같은 방의 다른 사용자에게만 메시지 전송
    private void broadcastToOther(Long roomId, WebSocketSession senderSession, String message){
        Set<WebSocketSession> sessions = chatRooms.getOrDefault(roomId, Collections.emptySet());
        for (WebSocketSession s : sessions) {
            if (!s.equals(senderSession)) {  // 메시지 보낸 사람 제외
                sendMessageSafe(s, message);
            }
        }
    }

    private void broadcast(Long roomId, String message)  {
        Set<WebSocketSession> sessions = chatRooms.getOrDefault(roomId, Collections.emptySet());
        for (WebSocketSession s : sessions) {
            sendMessageSafe(s, message);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        chatRooms.values().forEach(sessions -> sessions.remove(session));
        log.info("연결 종료: {}", session.getId());
    }

    // 안전하게 메시지를 전송하는 유틸리티 메서드
    private void sendMessageSafe(WebSocketSession session, String message) {
        if (session == null || !session.isOpen()) {
            return; // 세션이 닫혔으면 무시
        }
        try {
            session.sendMessage(new TextMessage(message));
        } catch (Exception e) {
            log.error("메시지 전송 실패: sessionId={}, reason={}", session.getId(), e.getMessage(), e);
            try {
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("메세지 전송 실패")); // 문제가 생긴 세션은 정리
            } catch (Exception ignored) {}
        }
    }
}
