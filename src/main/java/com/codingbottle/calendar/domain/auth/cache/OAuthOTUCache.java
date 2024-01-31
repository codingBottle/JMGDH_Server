package com.codingbottle.calendar.domain.auth.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * OAuth 인증 중 서비스 서버가 발급한 일회용 인증코드를 담기 위한 캐시.
 * 아래 OAuth 인증 절차의 5~6번 단계를 처리하기 위해 사용된다.
 *
 * 1. 웹브라우저에서 백엔드 서버에 최초 요청
 * 2. 백엔드 서버는 요청을 받아서 OAuth 서버의 인증화면으로 리다이렉트시킴
 * 3. 웹브라우저에서 인증이 완료되면 OAuth 서버가 웹브라우저에 백엔드 서버로 리다이렉트 응답 (이 때 리다이렉트 url의 쿼리 파라미터에 인증코드가 포함되어 있음)
 * 4. 백엔드 서버는 리다이렉트된 요청을 받아 인증코드를 검증하고 최종적으로 사용자 정보를 얻는다.
 *  - 이 단계가 마무리되면 실질적인 인증과 회원가입 절차는 끝났고, 웹브라우저에 인증토큰을 발급할 일만 남았음.
 * 5. 이후 사용자를 프론트 서버로 다시 보내야 하므로, 프론트 도메인 주소로 리다이렉트시킨다.
 *  이 때, 리다이렉트시의 쿼리 파라미터에 일회용 인증코드를 보낸다.
 * 6. 프론트 서버는 일회용 인증코드를 백엔드 서버에 보내서 최종적으로 토큰을 발급받는다.
 */
@Component
public class OAuthOTUCache {

    // <verification code, memberId(in domain.member.entity.Member)>.
    // thread-safe map.
    private final Cache<String, Long> codeExpirationCache = Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build();

    public String putVerificationCodeInCache(long targetMemberId) {
        String verificationCode = UUID.randomUUID().toString();
        codeExpirationCache.put(verificationCode, targetMemberId);

        return verificationCode;
    }

    public long getMemberId(String verificationCode) {
        if (!StringUtils.hasText(verificationCode)) {
            throw new IllegalArgumentException("verificationCode must not be null or empty");
        }
        Long memberId = codeExpirationCache.getIfPresent(verificationCode);
        codeExpirationCache.invalidate(verificationCode);
        return Objects.requireNonNull(memberId, "verificationCode is invalid");
    }
}
