package com.season.livingmate.chat.api.dto.request;

import com.season.livingmate.chat.domain.ChatRoom;
import com.season.livingmate.chat.domain.ChatRoomStatus;
import com.season.livingmate.post.domain.Post;
import com.season.livingmate.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅방생성 DTO")
public class ChatRoomReqDto {

    @Schema(description = "채팅을 보낼 게시물 아이디", example = "1")
    @NotNull
    private Long postId;

    public ChatRoom toEntity(User user, Post post){
        return ChatRoom.builder()
                .post(post)
                .sender(user)
                .receiver(post.getUser())
                .chatRoomStatus(ChatRoomStatus.PENDING)
                .build();
    }
}