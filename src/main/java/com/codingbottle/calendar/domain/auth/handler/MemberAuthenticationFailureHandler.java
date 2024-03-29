package com.codingbottle.calendar.domain.auth.handler;

import com.codingbottle.calendar.global.utils.ErrorResponder;
import com.codingbottle.calendar.global.exception.common.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


// 로그인 인증 실패 시 동작되는 핸들러
@Slf4j
public class MemberAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        log.info("Authenticated Failed");
        ErrorResponder.sendErrorResponse(response, request, ErrorCode.MISMATCHED_SIGNIN_INFO, exception);

    }
}