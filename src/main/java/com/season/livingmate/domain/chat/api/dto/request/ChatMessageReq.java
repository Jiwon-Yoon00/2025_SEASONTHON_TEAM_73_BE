package com.season.livingmate.domain.chat.api.dto.request;

import com.season.livingmate.domain.chat.domain.ChatRoom;
import com.season.livingmate.domain.chat.domain.Message;
import com.season.livingmate.domain.chat.domain.MessageType;
import com.season.livingmate.domain.chat.domain.WebSocketType;
import com.season.livingmate.domain.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "메세지 요청 DTO")
public class ChatMessageReq {

    @Schema(description = "메세지 타입", example = "TEXT", allowableValues = {"TEXT", "IMAGE", "FILE", "SYSTEM"})
    @NotNull
    private MessageType type;

    @Schema(description = "웹소켓 타입", example = "SEND", allowableValues = {"JOIN", "SEND", "READ"})
    @NotNull
    private WebSocketType websocket;

    @Schema(description = "메세지 내용", example = "안녕하세요! 룸메로 지원하고 싶습니다!")
    @NotBlank
    private String content;

    @Schema(description = "채팅방아이디", example = "1")
    private Long chatRoomId;

    @Schema(description = "게시물아이디", example = "1")
    private Long postId;

    public Message toEntity(User user, ChatRoom chatRoom){
        return Message.builder()
                .type(type)
                .content(content)
                .sender(user)
                .isRead(false)
                .chatRoom(chatRoom)
                .build();
    }
}
