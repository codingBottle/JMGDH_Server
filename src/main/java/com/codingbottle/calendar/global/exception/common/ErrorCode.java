package com.codingbottle.calendar.global.exception.common;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Jackson HTTP BODY 파싱
    HTTP_MESSAGE_NOT_READABLE(400, "요청값을 읽어들이지 못했습니다. 형식을 확인해 주세요."),

    // 회원가입
    EMAIL_DUPLICATION(400, "이미 존재하는 이메일입니다."),

    // 인증 - 로그인 시도
    MISMATCHED_SIGNIN_INFO(401, "잘못된 로그인 정보입니다."),
    USER_NOT_FOUND_AT_LOGIN(401, "존재하지 않는 계정입니다. 회원가입 후 로그인해주세요."),
    MISMATCHED_SIGNIN_TYPE(401, "이미 가입된 회원입니다. 다른 로그인 방법을 확인해주세요."),

    // 인증 - 토큰
    NOT_EXISTS_AUTH_HEADER(401, "Authorization Header가 빈 값입니다."),
    NOT_VALID_BEARER_GRANT_TYPE(401, "인증 타입이 Bearer 타입이 아닙니다."),
    TOKEN_EXPIRED(401, "해당 token은 만료되었습니다."),
    INVALID_SIGNATURE(401, "토큰의 서명이 유효하지 않습니다."),
    MALFORMED_TOKEN(401, "잘못된 형식의 JWT가 주어졌습니다."),
    ACCESS_TOKEN_EXPIRED(401, "해당 access token은 만료되었습니다."),
    NOT_ACCESS_TOKEN_TYPE(401, "tokenType이 access token이 아닙니다."),
    REFRESH_TOKEN_EXPIRED(401, "해당 refresh token은 만료되었습니다."),
    REFRESH_TOKEN_NOT_FOUND(400, "해당 refresh token은 존재하지 않습니다."),
    NOT_VALID_TOKEN(401, "유효하지 않은 토큰입니다."),
    ACCESS_DENIED(403, "인증은 되어 있지만, 특정 리소스에 대한 접근 권한이 없습니다."),

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
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생하였습니다."),

    // utils/converter
    DATETIME_IS_NULL(401, "DateTime이 NULL값입니다."),
    DATE_IS_NULL(401, "Date가 NULL값입니다.");

    private final int statusCode;
    private final String message;

    ErrorCode(int status, String message) {
        this.statusCode = status;
        this.message = message;
    }

}
