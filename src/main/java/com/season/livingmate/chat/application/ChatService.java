package com.season.livingmate.chat.application;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.chat.api.dto.request.ChatMessageReqDto;
import com.season.livingmate.chat.api.dto.request.ChatRoomReqDto;
import com.season.livingmate.chat.api.dto.response.ChatMessageResDto;
import com.season.livingmate.chat.api.dto.response.ChatRoomResDto;
import com.season.livingmate.chat.domain.ChatRoom;
import com.season.livingmate.chat.domain.ChatRoomStatus;
import com.season.livingmate.chat.domain.Message;
import com.season.livingmate.chat.domain.repository.ChatRoomRepository;
import com.season.livingmate.chat.domain.repository.MessageRepository;
import com.season.livingmate.exception.CustomException;
import com.season.livingmate.exception.status.ErrorStatus;
import com.season.livingmate.post.domain.Post;
import com.season.livingmate.post.domain.repository.PostRepository;
import com.season.livingmate.user.domain.User;
import com.season.livingmate.user.domain.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 채팅방 생성
    @Transactional
    public ChatRoomResDto createChatRoom(Long postId, CustomUserDetails userDetails) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.RESOURCE_NOT_FOUND));

        User user = userDetails.getUser();

        // 게시글 작성자가 스스로 채팅방을 생성하려는 경우 예외 처리
        if (post.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorStatus.FORBIDDEN);
        }

        // 채팅방 중복 체크
        Optional<ChatRoom> existRoom = chatRoomRepository.findByPost_PostIdAndSender_Id(post.getPostId(), user.getId());
        if (existRoom.isPresent()) {

            ChatRoom room = existRoom.get();
            if (room.getChatRoomStatus() == ChatRoomStatus.PENDING) {
                // 상태가 PENDING이면 새 채팅 생성 불가
                throw new CustomException(ErrorStatus.FORBIDDEN);
            }
            return ChatRoomResDto.from(existRoom.get()); // 이미 존재하는 채팅방 반환
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .post(post)
                .sender(user)
                .receiver(post.getUser())
                .build();

        chatRoomRepository.save(chatRoom);
        return ChatRoomResDto.from(chatRoom);
    }

    // 메세지 생성(저장)
    @Transactional
    public ChatMessageResDto createMessage(ChatMessageReqDto chatMessageReqDto, CustomUserDetails userDetails) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageReqDto.getChatRoomId())
                .orElseThrow(() -> new CustomException(ErrorStatus.RESOURCE_NOT_FOUND));

        // 채팅방 수락 상태 확인
        if (chatRoom.getChatRoomStatus() != ChatRoomStatus.ACCEPTED) {
            throw new CustomException(ErrorStatus.ONLY_SENDER_CANNOT_CREATE);
        }

        User user = userDetails.getUser();

        Message message = chatMessageReqDto.toEntity(user, chatRoom);
        messageRepository.save(message);
        return ChatMessageResDto.from(message);

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
    public List<ChatMessageResDto> getMessagesByRoom(Long chatRoomId, CustomUserDetails userDetails, int page, int size) {

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
                .map(ChatMessageResDto::from)
                .collect(Collectors.toList());
    }

    // 요청자가 게시물 작성자에게 채팅 신청
    @Transactional
    public ChatRoomResDto requestChatRoom(Long postId, CustomUserDetails userDetails){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorStatus.RESOURCE_NOT_FOUND));

        User user = userDetails.getUser();

        if (post.getUser().getId().equals(user.getId())) { // post 작성자와 신청자가 같으면 안됨
            throw new CustomException(ErrorStatus.FORBIDDEN);
        }

        Optional<ChatRoom> existRoom =  chatRoomRepository.findByPost_PostIdAndSender_Id(post.getPostId(), user.getId());
        if(existRoom.isPresent()){
            throw new CustomException(ErrorStatus.CHAT_ROOM_ALREADY_APPLIED); // 이미 신청한 경우 예외
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .post(post)
                .sender(user)
                .receiver(post.getUser())
                .chatRoomStatus(ChatRoomStatus.PENDING)
                .build();

        chatRoomRepository.save(chatRoom);
        return  ChatRoomResDto.from(chatRoom);

    }

    // 작성자가 신청자 목록 조회
    @Transactional(readOnly = true)
    public Map<ChatRoomStatus, List<ChatRoomResDto>> getChatRoomsByStatusForReceiver(CustomUserDetails userDetails) {
        User receiver = userDetails.getUser();

        // 작성자가 받은 모든 상태 채팅방 조회
        List<ChatRoom> rooms = chatRoomRepository.findByReceiver_IdAndChatRoomStatusIn(
                receiver.getId(),
                List.of(ChatRoomStatus.PENDING, ChatRoomStatus.ACCEPTED)
        );

        return rooms.stream()
                .map(ChatRoomResDto::from)
                .collect(Collectors.groupingBy(ChatRoomResDto::getChatRoomStatus));
    }


    // 작성자가 수락
    @Transactional
    public ChatRoomResDto acceptChatRoom(Long chatRoomId, CustomUserDetails userDetails) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorStatus.RESOURCE_NOT_FOUND));

        if (!chatRoom.getReceiver().getId().equals(userDetails.getUser().getId())) {
            throw new CustomException(ErrorStatus.ONLY_RECEIVER_CAN_ACCEPT);
        }

        chatRoom.setChatRoomStatus(ChatRoomStatus.ACCEPTED);
        chatRoomRepository.save(chatRoom);

        return ChatRoomResDto.from(chatRoom);
    }

    // 작성자가 거절
    @Transactional
    public void rejectChatRoom(Long chatRoomId, CustomUserDetails userDetails) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new CustomException(ErrorStatus.RESOURCE_NOT_FOUND));

        if (!chatRoom.getReceiver().getId().equals(userDetails.getUser().getId())) {
            throw new CustomException(ErrorStatus.ONLY_RECEIVER_CAN_REJECT);
        }

        chatRoomRepository.delete(chatRoom);
    }

    @Transactional(readOnly = true)
    public Map<ChatRoomStatus, List<ChatRoomResDto>> getMyChatRoomsByStatus(CustomUserDetails userDetails) {
        User requester = userDetails.getUser();

        List<ChatRoom> rooms = chatRoomRepository.findBySender_IdAndChatRoomStatusIn(
                requester.getId(),
                List.of(ChatRoomStatus.PENDING, ChatRoomStatus.ACCEPTED)
        );

        return rooms.stream()
                .map(ChatRoomResDto::from)
                .collect(Collectors.groupingBy(ChatRoomResDto::getChatRoomStatus));
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
