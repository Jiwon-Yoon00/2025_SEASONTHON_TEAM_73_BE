package com.season.livingmate.chat.domain.repository;

import com.season.livingmate.chat.domain.ChatRoom;
import com.season.livingmate.chat.domain.ChatRoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    // 특정 게시글에 대해 특정 사용자가 이미 채팅방을 생성했는지 확인
    Optional<ChatRoom> findByPost_PostIdAndSender_Id(Long postId, Long senderId);

    List<ChatRoom> findByReceiver_IdOrSender_IdAndChatRoomStatusIn(Long receiverId, Long senderId, Collection<ChatRoomStatus> status);
}
