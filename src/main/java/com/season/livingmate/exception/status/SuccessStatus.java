package com.season.livingmate.exception.status;

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
    DELETE_POST(HttpStatus.OK, "POST201", "게시글이 성공적으로 삭제되었습니다."),;

    private final HttpStatus status;
    private final String code;
    private final String message;
}
