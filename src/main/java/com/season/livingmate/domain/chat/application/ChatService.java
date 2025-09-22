package com.season.livingmate.domain.chat.application;

import com.season.livingmate.domain.auth.security.CustomUserDetails;
import com.season.livingmate.domain.chat.api.dto.request.ChatMessageReq;
import com.season.livingmate.domain.chat.api.dto.response.ChatMessageRes;
import com.season.livingmate.domain.chat.api.dto.response.ChatRoomRes;
import com.season.livingmate.domain.chat.domain.ChatRoom;
import com.season.livingmate.domain.chat.domain.ChatRoomStatus;
import com.season.livingmate.domain.chat.domain.Message;
import com.season.livingmate.domain.chat.domain.repository.ChatRoomRepository;
import com.season.livingmate.domain.chat.domain.repository.MessageRepository;
import com.season.livingmate.global.exception.CustomException;
import com.season.livingmate.global.exception.status.ErrorStatus;
import com.season.livingmate.domain.post.domain.Post;
import com.season.livingmate.domain.post.domain.repository.PostRepository;
import com.season.livingmate.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final PostRepository postRepository;

    // 채팅방 생성
    @Transactional
    public ChatRoomRes createChatRoom(Long postId, CustomUserDetails userDetails) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.RESOURCE_NOT_FOUND));

        User user = userDetails.getUser();

        // 게시글 작성자가 스스로 채팅방을 생성하려는 경우 예외 처리
        if (post.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorStatus.ONLY_SENDER_CANNOT_CREATE);
        }

        // 채팅방 중복 체크
        Optional<ChatRoom> existRoom = chatRoomRepository.findByPost_PostIdAndSender_Id(post.getPostId(), user.getId());
        if (existRoom.isPresent()) {

            ChatRoom room = existRoom.get();
            if (room.getChatRoomStatus() == ChatRoomStatus.PENDING) {
                // 상태가 PENDING이면 새 채팅 생성 불가
                throw new CustomException(ErrorStatus.FORBIDDEN);
            }
            return ChatRoomRes.from(existRoom.get()); // 이미 존재하는 채팅방 반환
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .post(post)
                .sender(user)
                .receiver(post.getUser())
                .chatRoomStatus(ChatRoomStatus.PENDING)
                .build();

        chatRoomRepository.save(chatRoom);
        return ChatRoomRes.from(chatRoom);
    }

    // 메세지 생성(저장)
    @Transactional
    public ChatMessageRes createMessage(ChatMessageReq chatMessageReq, CustomUserDetails userDetails) {
        User user = userDetails.getUser();

        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageReq.getChatRoomId())
                .orElseThrow(() -> new CustomException(ErrorStatus.RESOURCE_NOT_FOUND));

        // 내가 속한 채팅방인지 확인
        if (!chatRoom.getSender().getId().equals(userDetails.getUserId()) &&
                !chatRoom.getReceiver().getId().equals(userDetails.getUserId())) {
            throw new CustomException(ErrorStatus.FORBIDDEN);
        }

        Message message = chatMessageReq.toEntity(user, chatRoom);
        messageRepository.save(message);
        return ChatMessageRes.from(message);

    }

    // 읽음 처리
    @Transactional
    public void markMessageAsRead(Long roomId, Long readerId) {

        List<Message> unreadMessages = messageRepository
                .findByChatRoomIdAndIsReadFalseAndSenderIdNot(roomId, readerId);

        for (Message message : unreadMessages) {
            message.setRead(true); // 모두 읽음 처리
        }
    }

    // 과거 메세지 조회하기
    @Transactional(readOnly = true)
    public List<ChatMessageRes> getMessagesByRoom(Long chatRoomId, CustomUserDetails userDetails, int page, int size) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorStatus.RESOURCE_NOT_FOUND));

        // 내가 속한 채팅방인지 확인
        if (!chatRoom.getSender().getId().equals(userDetails.getUserId()) &&
                !chatRoom.getReceiver().getId().equals(userDetails.getUserId())) {
            throw new CustomException(ErrorStatus.FORBIDDEN);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        return messageRepository.findByChatRoom_Id(chatRoomId, pageable)
                .stream()
                .map(ChatMessageRes::from)
                .collect(Collectors.toList());
    }

    // 작성자가 수락
    @Transactional
    public ChatRoomRes acceptChatRoom(Long chatRoomId, CustomUserDetails userDetails) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorStatus.RESOURCE_NOT_FOUND));

        if (!chatRoom.getReceiver().getId().equals(userDetails.getUser().getId())) {
            throw new CustomException(ErrorStatus.ONLY_RECEIVER_CAN_ACCEPT);
        }


        // 상태는 ACCEPTED로 변경
        chatRoom.setChatRoomStatus(ChatRoomStatus.ACCEPTED);
        chatRoomRepository.save(chatRoom);

        return ChatRoomRes.from(chatRoom);
    }

    // 작성자가 거절
    @Transactional
    public void rejectChatRoom(Long chatRoomId, CustomUserDetails userDetails) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorStatus.RESOURCE_NOT_FOUND));

        if (!chatRoom.getReceiver().getId().equals(userDetails.getUser().getId())) {
            throw new CustomException(ErrorStatus.ONLY_RECEIVER_CAN_REJECT);
        }

        // 채탕방 삭제
        chatRoomRepository.delete(chatRoom);
        //메세지 삭제
        messageRepository.deleteAll(chatRoom.getMessages());
    }

    //채팅방 목록 조회
    @Transactional(readOnly = true)
    public Map<ChatRoomStatus, List<ChatRoomRes>> getMyChatRoomsByStatus(CustomUserDetails userDetails) {
        User requester = userDetails.getUser();

        List<ChatRoom> rooms = chatRoomRepository.findByReceiver_IdOrSender_IdAndChatRoomStatusIn(
                requester.getId(),
                requester.getId(),
                List.of(ChatRoomStatus.PENDING, ChatRoomStatus.ACCEPTED)
        );

        return rooms.stream()
                .map(ChatRoomRes::from)
                .collect(Collectors.groupingBy(ChatRoomRes::getChatRoomStatus));
    }

    // 채팅방 삭제
    @Transactional
    public void delete(Long chatRoomId, CustomUserDetails userDetails) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorStatus.RESOURCE_NOT_FOUND));


        if (!chatRoom.getSender().getId().equals(userDetails.getUserId()) &&
                !chatRoom.getReceiver().getId().equals(userDetails.getUserId())) {
            throw new CustomException(ErrorStatus.CHAT_ROOM_DELETE_FORBIDDEN);
        }

        messageRepository.deleteAll(chatRoom.getMessages());
        chatRoomRepository.delete(chatRoom);
    }
}
