package com.season.livingmate.chat.application;

import com.season.livingmate.auth.security.CustomUserDetails;
import com.season.livingmate.chat.api.dto.request.ChatMessageReqDto;
import com.season.livingmate.chat.api.dto.request.ChatRoomReqDto;
import com.season.livingmate.chat.api.dto.response.ChatMessageResDto;
import com.season.livingmate.chat.api.dto.response.ChatRoomResDto;
import com.season.livingmate.chat.domain.ChatRoom;
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
    public ChatRoomResDto createChatRoom(ChatRoomReqDto chatRoomReqDto, CustomUserDetails userDetails) {
        Post post = postRepository.findById(chatRoomReqDto.getPostId())
                .orElseThrow(() -> new CustomException(ErrorStatus.RESOURCE_NOT_FOUND));

        User user = userDetails.getUser();

        // 채팅방 중복 체크
        Optional<ChatRoom> existRoom = chatRoomRepository.findByPost_PostIdAndSender_Id(post.getPostId(), user.getId());
        if (existRoom.isPresent()) {
            return ChatRoomResDto.from(existRoom.get()); // 이미 존재하는 채팅방 반환
        }

        ChatRoom chatRoom = chatRoomReqDto.toEntity(user, post);
        chatRoomRepository.save(chatRoom);
        return ChatRoomResDto.from(chatRoom);
    }

    // 메세지 생성(저장)
    @Transactional
    public ChatMessageResDto createMessage(ChatMessageReqDto chatMessageReqDto, CustomUserDetails userDetails) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageReqDto.getChatRoomId())
                .orElseThrow(() -> new CustomException(ErrorStatus.RESOURCE_NOT_FOUND));

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
            throw new CustomException(ErrorStatus.FORBIDDEN); // 403
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());

        return messageRepository.findByChatRoom_Id(chatRoomId, pageable)
                .stream()
                .map(ChatMessageResDto::from)
                .collect(Collectors.toList());
    }


}
