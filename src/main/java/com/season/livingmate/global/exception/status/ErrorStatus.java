package com.season.livingmate.global.exception.status;

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

    // 채팅방 권한 관련
    ONLY_RECEIVER_CAN_ACCEPT(HttpStatus.FORBIDDEN, "CHAT403", "작성자만 수락할 수 있습니다."),
    ONLY_RECEIVER_CAN_REJECT(HttpStatus.FORBIDDEN, "CHAT403", "작성자만 거절할 수 있습니다."),
    CHAT_ROOM_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "CHAT403", "채팅방 삭제 권한이 없습니다."),
    ONLY_SENDER_CANNOT_CREATE(HttpStatus.FORBIDDEN, "CHAT403", "작성자는 스스로 채팅방을 생성할 수 없습니다."),
    CHAT_ROOM_NOT_ACCEPTED(HttpStatus.FORBIDDEN, "CHAT403", "수락된 채팅방에서만 메시지를 보낼 수 있습니다."),
    // 상태 관련
    CHAT_ROOM_ALREADY_APPLIED(HttpStatus.CONFLICT, "CHAT409", "이미 신청된 채팅방입니다."),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "PROFILE400", "프로필이 존재하지 않습니다."),

    // ai
    GPT_CALL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "GPT500", "GPT API 호출에 실패했습니다."),
    USER_SURVEY_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "USER400", "성향조사가 완료되지 않은 사용자입니다."),
    RECOMMENDATION_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "RECOMMEND500", "추천 서비스에 오류가 발생했습니다."),
    INVALID_WEIGHT_COUNT(HttpStatus.BAD_REQUEST, "WEIGHT400", "정확히 3개의 성향조사 항목을 선택해주세요."),
    INVALID_WEIGHT_SUM(HttpStatus.BAD_REQUEST, "WEIGHT400", "가중치의 합이 100이 되어야 합니다."),
    INVALID_SURVEY_FIELD(HttpStatus.BAD_REQUEST, "SURVEY4003", "유효하지 않은 성향조사 항목입니다."),
    INVALID_SELECTED_ITEMS_COUNT(HttpStatus.BAD_REQUEST, "SELECT400", "3개의 항목을 선택해야 합니다."),
    INVALID_SELECTED_ITEMS_FIELD(HttpStatus.BAD_REQUEST, "SELECT400", "유효하지 않은 성향조사 항목입니다."),
    DUPLICATE_SELECTED_ITEMS(HttpStatus.BAD_REQUEST, "SELECT400", "중복된 항목을 선택할 수 없습니다."),
    RECOMMENDATION_WEIGHTS_NOT_SET(HttpStatus.BAD_REQUEST, "WEIGHT400", "추천 가중치가 설정되지 않았습니다."),

    // user profile like
    ALREADY_LIKED(HttpStatus.BAD_REQUEST, "LIKE400", "이미 좋아요를 눌렀습니다."),
    SELF_LIKE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "LIKE400", "자기 자신은 좋아요할 수 없습니다."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "LIKE404", "좋아요를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
