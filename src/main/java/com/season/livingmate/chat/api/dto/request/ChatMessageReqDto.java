package com.season.livingmate.chat.api.dto.request;

import com.season.livingmate.chat.domain.ChatRoom;
import com.season.livingmate.chat.domain.Message;
import com.season.livingmate.chat.domain.MessageType;
import com.season.livingmate.chat.domain.WebSocketType;
import com.season.livingmate.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.WeakHashMap;

@Getter
@Setter
@Schema(description = "메세지 생성 DTO")
public class ChatMessageReqDto {

    @Schema(description = "메세지 타입", example = "TEXT", allowableValues = {"TEXT", "IMAGHE", "FILE", "SYSTEM"})
    @NotNull
    private MessageType type;

    @Schema(description = "웹소켓 타입", example = "SEND", allowableValues = {"JOIN", "SEND", "READ"})
    @NotNull
    private WebSocketType websocket;

    @Schema(description = "메세지 내용", example = "안녕하세요! 룸메로 지원하고 싶습니다!")
    @NotBlank
    private String content;

    @Schema(description = "채팅방아이디", example = "1")
    @NotNull
    private Long chatRoomId;

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
