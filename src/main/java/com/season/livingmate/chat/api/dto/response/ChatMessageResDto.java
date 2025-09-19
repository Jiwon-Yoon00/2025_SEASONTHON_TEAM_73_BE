package com.season.livingmate.chat.api.dto.response;


import com.season.livingmate.chat.domain.Message;
import com.season.livingmate.chat.domain.MessageType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅메세지 응답 DTO")
public class ChatMessageResDto {

    @Schema(description = "메세지 타입", example = "TEXT")
    private MessageType type;

    @Schema(description = "메세지 ID", example = "1")
    private Long messageId;

    @Schema(description = "채팅방 ID", example = "1")
    private Long chatRoomId;

    @Schema(description = "보낸사람 ID", example = "1")
    private Long senderId;

    @Schema(description = "보낸사람 nickname", example = "0yuniverse0")
    private String senderName;

    @Schema(description = "보낸 사람 프로필", example = "url")
    private String senderProfile;

    @Schema(description = "메세지 내용", example = "안녕하세요! 룸메로 지원하고 싶습니다!")
    private String content;

    @Schema(description = "생성일시", example = "2025-08-31T12:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "읽음여부", example = "false")
    private boolean read;

    public static ChatMessageResDto from(Message message) {
        return ChatMessageResDto.builder()
                .messageId(message.getId())
                .chatRoomId(message.getChatRoom().getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getNickname())
                .senderProfile(message.getSender().getUserProfile().getProfileImageUrl())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .read(message.isRead())
                .build();
    }
}
