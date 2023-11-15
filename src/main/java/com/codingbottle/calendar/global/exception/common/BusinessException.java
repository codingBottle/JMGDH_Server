package com.codingbottle.calendar.global.exception.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

//

/**
 * 비즈니스 로직 상 예외
 * ErrorCode에 적힌 기본 에러메시지를 사용할 땐 ErrorCode 생성자 사용.
 * 상세한 에러 메시지를 사용하고 싶을 땐 String message, HttpStatus httpStatus 생성자 사용.
 *
 * 예외 전환 시에는 원본 예외를 Throwable cause 로 넘겨준다.
 */
@Getter
public class BusinessException extends RuntimeException {
    private final int statusCode;
    private final HttpStatus httpStatus;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // throwable 의 detailMessage 에 들어가며, throwable.getMessage()로 부를 수 있다.
        this.statusCode = errorCode.getStatusCode();
        this.httpStatus = HttpStatus.valueOf(errorCode.getStatusCode());
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause); // throwable 의 detailMessage 에 들어가며, throwable.getMessage()로 부를 수 있다.
        this.statusCode = errorCode.getStatusCode();
        this.httpStatus = HttpStatus.valueOf(errorCode.getStatusCode());
    }

    public BusinessException(String message, HttpStatus httpStatus) {
        super(message); // throwable 의 detailMessage 에 들어가며, throwable.getMessage()로 부를 수 있다.
        this.statusCode = httpStatus.value();
        this.httpStatus = httpStatus;
    }

    public BusinessException(String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause); // throwable 의 detailMessage 에 들어가며, throwable.getMessage()로 부를 수 있다.
        this.statusCode = httpStatus.value();
        this.httpStatus = httpStatus;
    }
}