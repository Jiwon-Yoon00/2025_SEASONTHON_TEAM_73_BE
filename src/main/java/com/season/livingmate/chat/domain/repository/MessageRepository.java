package com.season.livingmate.chat.domain.repository;
import com.season.livingmate.chat.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {

    // 읽지 않은 메세지 조회
    // 메세지가 속한 채팅방 ID && is_read == false && senderId가 포함되지 않은 메세지
    List<Message> findByChatRoomIdAndIsReadFalseAndSenderIdNot(Long roomId, Long senderId);

    // 페이지 단위로 메세지 조회
    Page<Message> findByChatRoom_Id(Long chatRoomId, Pageable pageable);
}
