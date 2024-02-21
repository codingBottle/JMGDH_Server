package com.codingbottle.calendar.global.test;

import com.codingbottle.calendar.domain.auth.cache.OAuthOTUCache;
import com.codingbottle.calendar.domain.auth.jwt.JwtTokenizer;
import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class TestMemberTokenProvider {
    private final JwtTokenizer jwtTokenizer;
    private final OAuthOTUCache oAuthOTUCache;
    private final MemberService memberService;

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

    @GetMapping("/test/member/{memberId}/access-token")
    public String testMemberAccessToken(
            @PathVariable("memberId") Long memberId
    ) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("roles", List.of("USER"));
        return jwtTokenizer.generateAccessToken(map, memberId.toString());
    }

    @GetMapping("/test/members/generate-random-two")
    public String generateMemberWithRandomData() {
        String randomData = UUID.randomUUID().toString().substring(0, 6);
        Member member = memberService.createMember(randomData, randomData, "");
        Member member2 = memberService.createMember(randomData + "1" , randomData + "1", "");

        return member.getEmail() + " : " + member2.getEmail() + " created";
    }
}