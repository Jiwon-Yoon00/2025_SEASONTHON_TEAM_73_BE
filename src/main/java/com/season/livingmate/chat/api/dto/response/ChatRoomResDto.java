package com.season.livingmate.chat.api.dto.response;

import com.season.livingmate.chat.domain.ChatRoom;
import com.season.livingmate.chat.domain.ChatRoomStatus;
import com.season.livingmate.chat.domain.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅방 생성 응답 DTO")
public class ChatRoomResDto {

    @Schema(description = "채팅방 ID")
    private Long chatRoomId;

    @Schema(description = "게시물 ID")
    private Long postId;

    @Schema(description = "게시물 제목")
    private String postTitle;

    @Schema(description = "보낸 사람 ID")
    private Long senderId;

    @Schema(description = "보낸 사람 닉네임")
    private String senderName;

    @Schema(description = "받는 사람 ID")
    private Long receiverId;

    @Schema(description = "받는 사람 닉네임")
    private String receiverName;

    @Schema(description = "읽지 않은 메세지 수")
    private int unreadCount;

    @Schema(description = "마지막으로 보낸 메세지")
    private ChatMessageResDto lastMessage;

    @Schema(description = "수락상태")
    private ChatRoomStatus chatRoomStatus;

    @Schema(description = "채팅방 생성 시각")
    private LocalDateTime createdAt;

    public static ChatRoomResDto from(ChatRoom chatRoom) {
        List<Message> messages = Optional.ofNullable(chatRoom.getMessages()) // 채팅방의 메세지 가져오기
                .orElse(Collections.emptyList());


        int unreadCount = (int) Optional.ofNullable(chatRoom.getMessages())
                .orElse(Collections.emptyList())
                .stream()
                .filter(m -> !m.isRead())
                .count(); // 읽지 않은 메세지 수

        ChatMessageResDto lastMessage = messages.stream()
                .max(Comparator.comparing(Message::getCreatedAt)) // 가장 최근 메시지
                .map(ChatMessageResDto::from)
                .orElse(null); // 메세지 중에서, 마지막 메세지 가져오기

        return ChatRoomResDto.builder()
                .chatRoomId(chatRoom.getId())
                .postId(chatRoom.getPost().getPostId())
                .postTitle(chatRoom.getPost().getTitle())
                .senderId(chatRoom.getSender().getId())
                .senderName(chatRoom.getSender().getUsername())
                .receiverId(chatRoom.getReceiver().getId())
                .receiverName(chatRoom.getReceiver().getUsername())
                .unreadCount(unreadCount)
                .lastMessage(lastMessage)
                .chatRoomStatus(chatRoom.getChatRoomStatus())
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}
