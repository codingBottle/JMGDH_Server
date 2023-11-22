package com.codingbottle.calendar.domain.auth.filter;

import com.codingbottle.calendar.domain.auth.jwt.JwtTokenizer;
import com.codingbottle.calendar.global.utils.CustomAuthorityUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;

    public JwtVerificationFilter(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils) {

        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
    }

    // 검증 필터
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Claims claims = verifyJws(request);
            setAuthenticationToContext(claims);
        } catch (ExpiredJwtException ee) {
            request.setAttribute("exception", ee);
        } catch (MalformedJwtException me) {
            request.setAttribute("exception", me);
        } catch (SignatureException se) {
            request.setAttribute("exception", se);
        } catch (Exception e) {
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }

    // 검증 필터를 건너 띄어도 되는 경우
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String authorization = request.getHeader("Authorization");
        request.setAttribute("exception", null);
        // Authorization이 유효하지 않을 경우 true를 반환해서 필터를 실행하지 않도록 한다.
        return authorization == null || !authorization.startsWith("Bearer");
    }

    // 토큰 검증 메소드
    private Claims verifyJws(HttpServletRequest request) {
        String jws = request.getHeader("Authorization").replace("Bearer ", "");
        String secretKey = jwtTokenizer.getSecretKey();
        Claims claims = jwtTokenizer.getClaims(jws, secretKey).getBody();

        return claims;
    }

    // SecurityContextHolder에 Authentication 객체 올리는 메소드
    private void setAuthenticationToContext(Claims claims) {
        long memberId = Long.parseLong(claims.getAudience()); // claims에서 memberId를 받아온다.
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List) claims.get("roles")); // claims에 있는 roles를 바탕으로 권한을 만들어준다
        Authentication authentication = new UsernamePasswordAuthenticationToken(memberId, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContextHolder에 권한, 유저 객체 정보를 저장한다.
    }
}