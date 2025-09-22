package com.season.livingmate.domain.chat.api;

import com.season.livingmate.global.auth.security.CustomUserDetails;
import com.season.livingmate.domain.chat.api.dto.response.ChatMessageRes;
import com.season.livingmate.domain.chat.api.dto.response.ChatRoomRes;
import com.season.livingmate.domain.chat.application.ChatService;
import com.season.livingmate.domain.chat.domain.ChatRoomStatus;
import com.season.livingmate.global.exception.Response;
import com.season.livingmate.global.exception.status.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatrooms")
@Tag(name = "채팅API")
public class ChatController {

    private final ChatService chatService;

    // 채팅방 만들기
    @Operation(summary = "채팅방 생성 API")
    @PostMapping("/{postId}")
    public ResponseEntity<Response<ChatRoomRes>> createRoom(@PathVariable Long postId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ChatRoomRes chatRoomRes =  chatService.createChatRoom(postId, userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.CREATE_CHAT_ROOM, chatRoomRes));
    }

    // 채팅방 메세지 조회하기
    @Operation(summary = "메세지 조회 API")
    @GetMapping("/{chatRoomId}/messages")
    public ResponseEntity<Response<List<ChatMessageRes>>> getMessages(
            @PathVariable Long chatRoomId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<ChatMessageRes> messages = chatService.getMessagesByRoom(chatRoomId, userDetails, page, size);
        return ResponseEntity.ok(Response.success(SuccessStatus.GET_MESSAGES, messages));
    }

    // 작성자가 채팅 수락
    @Operation(summary = "채팅 신청 수락 API")
    @PostMapping("/accept/{chatRoomId}")
    public ResponseEntity<Response<ChatRoomRes>> acceptChatRoom(
            @PathVariable Long chatRoomId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        ChatRoomRes chatRoom = chatService.acceptChatRoom(chatRoomId, userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.ACCEPT_CHAT_ROOM, chatRoom));
    }

    // 작성자가 채팅 거절
    @Operation(summary = "채팅 신청 거절 API")
    @DeleteMapping("/reject/{chatRoomId}")
    public ResponseEntity<Response<Void>> rejectChatRoom(
            @PathVariable Long chatRoomId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        chatService.rejectChatRoom(chatRoomId, userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.REJECT_CHAT_ROOM, null));
    }

    // 채팅방 목록 조회
    @Operation(summary = "채팅방 목록 조회 API")
    @GetMapping("/lists")
    public ResponseEntity<Response<Map<ChatRoomStatus, List<ChatRoomRes>>>> getMyChatRoomsByStatus(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Map<ChatRoomStatus, List<ChatRoomRes>> rooms = chatService.getMyChatRoomsByStatus(userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.GET_CHAT_ROOMS, rooms));
    }

    // 채팅방 삭제
    @Operation(summary = "채팅방 삭제 API")
    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<Response<Void>> deleteChatRoom(
            @PathVariable Long chatRoomId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        chatService.delete(chatRoomId, userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.DELETE_CHAT_ROOM, null));
    }
}
