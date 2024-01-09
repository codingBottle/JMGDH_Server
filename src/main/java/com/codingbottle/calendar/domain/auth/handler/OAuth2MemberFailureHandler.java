package com.codingbottle.calendar.domain.auth.handler;

import com.codingbottle.calendar.global.exception.common.ErrorCode;
import com.codingbottle.calendar.global.utils.ErrorResponder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// OAuth2 로그인 실패했을 때 동작하는 핸들러
@Slf4j
public class OAuth2MemberFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("OAuth2 Login Failed!!");
        ErrorResponder.sendErrorResponse(response, request, ErrorCode.MISMATCHED_SIGNIN_TYPE, exception);
    }
}
