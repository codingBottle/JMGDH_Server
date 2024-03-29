//package com.codingbottle.calendar.domain.auth.filter;
//
//import com.codingbottle.calendar.domain.auth.dto.LoginDto;
//import com.codingbottle.calendar.domain.auth.jwt.JwtTokenizer;
//import com.codingbottle.calendar.domain.member.entity.Member;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    private final AuthenticationManager authenticationManager;
//    private final JwtTokenizer jwtTokenizer;
//
//    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenizer jwtTokenizer) {
//        this.authenticationManager = authenticationManager;
//        this.jwtTokenizer = jwtTokenizer;
//    }
//
//    @SneakyThrows // 예외 생략
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password());
//
//        return authenticationManager.authenticate(authenticationToken);
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request,
//                                            HttpServletResponse response,
//                                            FilterChain chain,
//                                            Authentication authResult) throws ServletException, IOException {
//        Member member = (Member) authResult.getPrincipal();
//
//        String accessToken = delegateAccessToken(member);
//        String refreshToken = delegateRefreshToken(member);
//
//        response.setHeader("Authorization", "Bearer " + accessToken);
//        response.setHeader("Refresh", refreshToken);
//
//        // 로그인 성공 핸들러 실행(MemberAuthenticationSuccessHandler)
//        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
//    }
//
//    private String delegateAccessToken(Member member) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("memberId", member.getMemberId()); // memberId 값 넣음
//        claims.put("roles", member.getRole());
//
//        String audience = String.valueOf(member.getMemberId()); // audience에 memberId 넣음
//
//        String accessToken = jwtTokenizer.generateAccessToken(claims, audience);
//
//        return accessToken;
//    }
//
//    private String delegateRefreshToken(Member member) {
//        String audience = String.valueOf(member.getMemberId()); // audience에 memberId 넣음
//        String refreshToken = jwtTokenizer.generateRefreshToken(audience);
//
//        return refreshToken;
//    }
//}