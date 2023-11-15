package com.codingbottle.calendar.global.exception.common;

import com.codingbottle.calendar.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 비즈니스 로직이 아닌 외부 환경에서 발생하는 예외
 * ErrorCode에 적힌 기본 에러메시지를 사용할 땐 ErrorCode 생성자 사용.
 * 상세한 에러 메시지를 사용하고 싶을 땐 String message, HttpStatus httpStatus 생성자 사용.
 *
 * 예외 전환 시에는 원본 예외를 Throwable cause 로 넘겨준다.
 */
@Getter
public class AppServiceException extends RuntimeException{
    private final int statusCode;
    private final HttpStatus httpStatus;

    public AppServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.statusCode = errorCode.getStatusCode();
        this.httpStatus = HttpStatus.valueOf(errorCode.getStatusCode());
    }

    public AppServiceException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.statusCode = errorCode.getStatusCode();
        this.httpStatus = HttpStatus.valueOf(errorCode.getStatusCode());
    }
}
