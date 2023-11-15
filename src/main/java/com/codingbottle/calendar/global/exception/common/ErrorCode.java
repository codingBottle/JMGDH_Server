package com.codingbottle.calendar.global.exception.common;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Jackson HTTP BODY 파싱
    HTTP_MESSAGE_NOT_READABLE(400, "요청값을 읽어들이지 못했습니다. 형식을 확인해 주세요."),

    // 인증 - 로그인 시도
    MISMATCHED_SIGNIN_INFO(400, "잘못된 로그인 정보입니다."),

    // 인증 - 토큰
    NOT_EXISTS_AUTH_HEADER(401, "Authorization Header가 빈 값입니다."),
    NOT_VALID_BEARER_GRANT_TYPE(401, "인증 타입이 Bearer 타입이 아닙니다."),
    TOKEN_EXPIRED(401, "해당 token은 만료되었습니다."),
    ACCESS_TOKEN_EXPIRED(401, "해당 access token은 만료되었습니다."),
    NOT_ACCESS_TOKEN_TYPE(401, "tokenType이 access token이 아닙니다."),
    REFRESH_TOKEN_EXPIRED(401, "해당 refresh token은 만료되었습니다."),
    REFRESH_TOKEN_NOT_FOUND(400, "해당 refresh token은 존재하지 않습니다."),
    NOT_VALID_TOKEN(401, "유효하지 않은 토큰입니다."),

    // 사용자
    USER_NOT_FOUND(404, "해당 사용자가 존재하지 않습니다."),

    // 파일
    INVALID_FILE_EXTENSION(400, "파일 확장자가 유효하지 않습니다."),
    FILE_NOT_FOUND(400, "해당 파일이 존재하지 않습니다."),
    FILE_CANNOT_BE_STORED(500, "파일을 저장할 수 없습니다."),
    FILE_CANNOT_BE_READ(500, "파일을 읽을 수 없습니다."),
    FILE_CANNOT_BE_SENT(500, "읽어들인 파일을 전송할 수 없습니다"),
    MULTIPART_FILE_CANNOT_BE_READ(500, "파일을 읽을 수 없습니다."),
    FILE_CANNOT_BE_DELETED(500, "파일을 삭제할 수 없습니다."),

    // INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생하였습니다.");
    ;

    private final int statusCode;
    private final String message;

    ErrorCode(int status, String message) {
        this.statusCode = status;
        this.message = message;
    }

}
