package com.codingbottle.calendar.global.test;

import com.codingbottle.calendar.domain.auth.cache.OAuthOTUCache;
import com.codingbottle.calendar.domain.auth.jwt.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class TestMemberTokenProvider {
    private final JwtTokenizer jwtTokenizer;
    private final OAuthOTUCache oAuthOTUCache;

    // 테스트 유저 (memberId: 1)의 토큰을 발급한다.
    @PostMapping("/test/token")
    public String testLogin() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("roles", List.of("USER"));
        return jwtTokenizer.generateAccessToken(map, "1");
    }

    @GetMapping("/test/otu-token")
    public String testOtuToken() {
        return oAuthOTUCache.putVerificationCodeInCache(1L);
    }
}
