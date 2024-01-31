package com.codingbottle.calendar.domain.auth.handler;

import com.codingbottle.calendar.domain.auth.cache.OAuthOTUCache;
import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@Component
@Slf4j
public class OAuth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final MemberService memberService;
    private final OAuthOTUCache oAuthOTUCache;

    public OAuth2MemberSuccessHandler(MemberService memberService, OAuthOTUCache oAuthOTUCache) {
        this.memberService = memberService;
        this.oAuthOTUCache = oAuthOTUCache;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var oAuth2User = (OAuth2User) authentication.getPrincipal(); // 로그인 된 구글 계정 사용자 정보를 받아온다
        String email = String.valueOf(oAuth2User.getAttributes().get("email")); // 로그인 된 구글 계정 사용자의 이메일을 받아온다.
        String nickname = String.valueOf(oAuth2User.getAttributes().get("name")); // 닉네임 불러오기

        Member member = memberService.oAuth2CheckMember(email, nickname);// 가입 되어 있으면 로그인, 없으면 회원가입
        redirect(request, response, member);
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, Member member) throws IOException {
        log.info("OAuth2 Login Success!!");
        String verificationCode = oAuthOTUCache.putVerificationCodeInCache(member.getId());
        String uri = createURI(verificationCode).toString();

        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    // OAuth2 로그인 성공 시 토큰값과 함께 반환될 URL 설정하는 부분
    private URI createURI(String verificationCode) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("oneTimeUseCode", verificationCode);

        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port(3000)
                .path("/oauth2/success")
                .queryParams(queryParams)
                .build()
                .toUri();
    }

}
