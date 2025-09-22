package com.season.livingmate.global.exception.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus {
    // SUCCESS 2XX
    SUCCESS(HttpStatus.OK, "COMMON200", "요청이 성공적으로 처리되었습니다."),
    CREATED(HttpStatus.CREATED, "COMMON201", "리소스가 성공적으로 생성되었습니다."),

    // 도메인별로
    // post
    CREATE_POST(HttpStatus.CREATED, "POST201", "게시글이 성공적으로 생성되었습니다."),
    GET_POST(HttpStatus.OK, "POST201", "게시글 단건이 성공적으로 조회되었습니다."),
    GET_POST_LIST(HttpStatus.OK, "POST201", "게시글 목록이 성공적으로 조회되었습니다."),
    UPDATE_POST(HttpStatus.OK, "POST201", "게시글이 성공적으로 수정되었습니다."),
    DELETE_POST(HttpStatus.OK, "POST201", "게시글이 성공적으로 삭제되었습니다."),


    // 유저프로필
    CREATE_PROFILE(HttpStatus.CREATED, "PROFILE201", "유저프로필이 성공적으로 생성되었습니다."),
    GET_MY_PROFILE(HttpStatus.OK, "PROFILE200", "나의 유저프로필이 성공적으로 조회되었습니다."),
    GET_PROFILE(HttpStatus.OK, "PROFILE200", "해당회원의 유저프로필이 성공적으로 조회되었습니다."),
    UPDATE_PROFILE(HttpStatus.OK, "PROFILE201", "유저프로필이 성공적으로 수정되었습니다."),


    // 채팅 & 채팅방
    GET_MESSAGES(HttpStatus.OK, "MESSAGE200", "채팅 메시지가 성공적으로 조회되었습니다."),
    CREATE_CHAT_ROOM(HttpStatus.CREATED, "CHAT201", "채팅방이 성공적으로 생성되었습니다."),
    DELETE_CHAT_ROOM(HttpStatus.OK, "CHAT200", "채팅방이 성공적으로 삭제되었습니다."),
    GET_CHAT_ROOMS(HttpStatus.OK, "CHAT200", "채팅 목록이 성공적으로 조회되었습니다."),
    GET_SENDER_CHAT_ROOMS(HttpStatus.OK, "CHAT200", "보낸 채팅 목록이 성공적으로 조회되었습니다."),

    // 채팅 신청
    APPLY_CHAT_ROOM(HttpStatus.CREATED, "CHAT201", "채팅 신청이 성공적으로 완료되었습니다."),
    ACCEPT_CHAT_ROOM(HttpStatus.OK, "CHAT200", "채팅 신청이 성공적으로 수락되었습니다."),
    REJECT_CHAT_ROOM(HttpStatus.OK, "CHAT200", "채팅 신청이 성공적으로 거절되었습니다."),


    // 좋아요
    CREATE_LIKE(HttpStatus.CREATED, "LIKE201", "좋아요가 성공적으로 생성되었습니다."),
    DELETE_LIKE(HttpStatus.OK, "LIKE200", "좋아요가 성공적으로 삭제되었습니다."),
    READ_LIKE(HttpStatus.OK, "LIKE200", "좋아요가 성공적으로 조회되었습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
