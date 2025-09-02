package com.season.livingmate.chat.api;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.chat.api.dto.request.ChatRoomReqDto;
import com.season.livingmate.chat.api.dto.response.ChatMessageResDto;
import com.season.livingmate.chat.api.dto.response.ChatRoomResDto;
import com.season.livingmate.chat.application.ChatService;
import com.season.livingmate.exception.Response;
import com.season.livingmate.exception.status.SuccessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chatrooms")
    public ResponseEntity<Response<ChatRoomResDto>> createRoom(@RequestBody ChatRoomReqDto chatRoomReqDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        ChatRoomResDto chatRoomResDto =  chatService.createChatRoom(chatRoomReqDto, userDetails);
        return ResponseEntity.ok(Response.success(SuccessStatus.CREATED, chatRoomResDto));
    }

    @GetMapping("/chatrooms/{chatRoomId}/messages")
    public ResponseEntity<Response<List<ChatMessageResDto>>> getMessages(
            @PathVariable Long chatRoomId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        List<ChatMessageResDto> messages = chatService.getMessagesByRoom(chatRoomId, userDetails, page, size);
        return ResponseEntity.ok(Response.success(SuccessStatus.GET_CHAT, messages));
    }
}
