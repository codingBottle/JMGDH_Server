package com.codingbottle.calendar.domain.auth.handler;

import com.codingbottle.calendar.domain.auth.jwt.JwtTokenizer;
import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class OAuth2MemberSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final MemberService memberService;
    private final JwtTokenizer jwtTokenizer;

    public OAuth2MemberSuccessHandler(OAuth2AuthorizedClientService authorizedClientService, MemberService memberService, JwtTokenizer jwtTokenizer) {
        this.authorizedClientService = authorizedClientService;
        this.memberService = memberService;
        this.jwtTokenizer = jwtTokenizer;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var oAuth2User = (OAuth2User) authentication.getPrincipal(); // 로그인 된 구글 계정 사용자 정보를 받아온다
        String email = String.valueOf(oAuth2User.getAttributes().get("email")); // 로그인 된 구글 계정 사용자의 이메일을 받아온다.
        String nickname = String.valueOf(oAuth2User.getAttributes().get("name")); // 닉네임 불러오기
        // var authorizedClient = authorizedClientService.loadAuthorizedClient("google", authentication.getName());

        memberService.oAuth2CheckMember(email, nickname); // 가입 되어 있으면 로그인, 없으면 회원가입

        Member member = memberService.getMemberByEmail(email);
        redirect(request, response, member);

    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, Member member) throws IOException {
        log.info("OAuth2 Login Success!!");

        String accessToken = delegateAccessToken(member);
        String refreshToken = delegateRefreshToken(member);

        String uri = createURI(accessToken, refreshToken).toString();
        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    private String delegateAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", member.getId()); // memberId 값 넣음
        claims.put("roles", member.getRole());

        String audience = String.valueOf(member.getId()); // audience에 memberId 넣음

        String accessToken = jwtTokenizer.generateAccessToken(claims, audience);

        return accessToken;
    }
    private String delegateRefreshToken(Member member) {
        String audience = String.valueOf(member.getId()); // audience에 memberId 넣음
        String refreshToken = jwtTokenizer.generateRefreshToken(audience);

        return refreshToken;
    }

    // OAuth2 로그인 성공 시 토큰값과 함께 반환될 URL 설정하는 부분
    private URI createURI(String accessToken, String refreshToken) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);


        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
//                .port(80)
                .path("/receive-token.html")
                .queryParams(queryParams)
                .build()
                .toUri();
    }

}
