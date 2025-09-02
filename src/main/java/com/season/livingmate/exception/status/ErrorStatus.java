package com.season.livingmate.exception.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    // COMMON 4XX
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "COMMON400", "파라미터가 올바르지 않습니다."),
    INVALID_BODY(HttpStatus.BAD_REQUEST, "COMMON400", "요청 본문이 올바르지 않습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "찾을 수 없는 리소스입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON404", "사용자를 찾을 수 없습니다"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON405", "허용되지 않는 HTTP Method입니다."),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "COMMON409", "이미 존재하는 리소스입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COOMON401", "권한이 없습니다."),

    // 도메인별로
    OTP_SEND_FAILED(HttpStatus.BAD_REQUEST, "OTP500", "OTP 전송에 실패했습니다."),
    OTP_MISMATCH(HttpStatus.BAD_REQUEST, "OTP400", "OTP가 일치하지 않습니다."),
    OTP_EXPIRED(HttpStatus.BAD_REQUEST, "OPT400", "OTP가 만료되었습니다"),
    OTP_NOT_FOUND(HttpStatus.BAD_REQUEST, "OPT400", "OTP정보가 없습니다."),

    // post
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "POST404", "게시글이 존재하지 않습니다."),

    ACCESS_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST,"TOKEN400","엑세스 토큰이 만료되었습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST,"TOKEN400","유효하지 않은 토큰입니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST,"TOKEN400","엑세스 토큰이 만료되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER500", "서버 오류가 발생했습니다."),

    // 채팅
    CREATE_CHAT_FAIL(HttpStatus.BAD_REQUEST, "CHAT401", "채팅 생성에 실패했습니다."),
    GET_CHAT_FAIL(HttpStatus.NOT_FOUND, "CHAT402", "해당 채팅을 찾을 수 없습니다."),
    GET_MY_CHATS_FAIL(HttpStatus.NOT_FOUND, "CHAT403", "나의 채팅 목록을 조회할 수 없습니다."),
    SEND_MESSAGE_FAIL(HttpStatus.BAD_REQUEST, "MESSAGE401", "메시지 전송에 실패했습니다."),
    GET_MESSAGES_FAIL(HttpStatus.NOT_FOUND, "MESSAGE402", "채팅 메시지를 조회할 수 없습니다."),
    READ_MESSAGE_FAIL(HttpStatus.BAD_REQUEST, "MESSAGE403", "메시지 읽음 처리에 실패했습니다."),
    ATTACH_FILE_FAIL(HttpStatus.BAD_REQUEST, "MESSAGE404", "첨부파일 전송에 실패했습니다."),
    JOIN_CHAT_FAIL(HttpStatus.BAD_REQUEST, "CHAT406", "채팅방 참여에 실패했습니다."),
    LEAVE_CHAT_FAIL(HttpStatus.BAD_REQUEST, "CHAT407", "채팅방 퇴장에 실패했습니다."),;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
