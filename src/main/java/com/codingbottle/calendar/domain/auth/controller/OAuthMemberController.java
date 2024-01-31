package com.codingbottle.calendar.domain.auth.controller;

import com.codingbottle.calendar.domain.auth.cache.OAuthOTUCache;
import com.codingbottle.calendar.domain.auth.dto.response.TokenResponse;
import com.codingbottle.calendar.domain.auth.jwt.JwtTokenizer;
import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.service.MemberService;
import com.codingbottle.calendar.global.api.RspTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth")
public class OAuthMemberController {
    private final OAuthOTUCache oAuthOTUCache;
    private final JwtTokenizer jwtTokenizer;
    private final MemberService memberService;

    @GetMapping("/token")
    public RspTemplate<TokenResponse> getToken(String code) {
        long memberId = oAuthOTUCache.getMemberId(code);
        Member member = memberService.getById(memberId);

        TokenResponse tokenResponse = jwtTokenizer.generateTokens(member);
        return new RspTemplate<>(HttpStatus.OK, "토큰 발급 성공", tokenResponse);
    }
}