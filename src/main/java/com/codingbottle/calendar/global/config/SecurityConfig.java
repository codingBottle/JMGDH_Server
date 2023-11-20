package com.codingbottle.calendar.global.config;

import com.codingbottle.calendar.domain.auth.filter.JwtVerificationFilter;
import com.codingbottle.calendar.domain.auth.handler.MemberAccessDeniedHandler;
import com.codingbottle.calendar.domain.auth.handler.MemberAuthenticationEntryPoint;
import com.codingbottle.calendar.domain.auth.handler.MemberAuthenticationFailureHandler;
import com.codingbottle.calendar.domain.auth.handler.MemberAuthenticationSuccessHandler;
import com.codingbottle.calendar.domain.auth.filter.JwtAuthenticationFilter;
import com.codingbottle.calendar.domain.auth.jwt.JwtTokenizer;
import com.codingbottle.calendar.domain.member.service.MemberService;
import com.codingbottle.calendar.global.utils.CustomAuthorityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberService memberService;
    // @Lazy는 빈 객체끼리 순환 참조를 막기 위해 사용
    public SecurityConfig(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils, @Lazy MemberService memberService) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.memberService = memberService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())    // cors 설정
                .and()
                .csrf().disable()        // Csrf 비활성화
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 이용하지 않는다(각 요청마다 사용자를 새롭게 인증한다)
                .and()
                .formLogin().disable()   // Form login 비활성화
                .httpBasic().disable()   // Header에 로그인 정보를 담는 방식 비활성화
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint())  // AuthenticationException이 발생할 때 실행되는 핸들러
                .accessDeniedHandler(new MemberAccessDeniedHandler())            // 인증은 됐지만 권한이 없을 때 실행되는 핸들러
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers(HttpMethod.POST, "/members").permitAll()
                        .anyRequest().authenticated() // 모든 접근 비허용 후 화이트리스트 기반 인증
                );
        return http.build();
    }

    // passwordEncoder 객체
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowCredentials(true); // 인증 정보 포함 가능
        configuration.setAllowedOrigins(List.of(
                "*"                              // 모든 경로 CORS 허용(추후 Client URL 허용)
        ));
        configuration.setAllowedMethods(List.of("GET","POST", "PATCH", "DELETE")); // 허용된 HTTP 메서드
        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
        configuration.setExposedHeaders(List.of("*")); // 모든 응답 헤더 표시

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {  // 커스텀 필터
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);  // JWT 인증 필터 선언
            jwtAuthenticationFilter.setFilterProcessesUrl("/login");    // login을 수행할 엔드포인트
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler()); // 로그인 성공 핸들러 추가
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler()); // 로그인 실패 핸들러 추가

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils, memberService); // JWT 검증 필터 선언

            builder
                    .addFilter(jwtAuthenticationFilter) // 필터에 JWT 인증 필터를 추가한다
                    .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class); // 필터에 JWT 검증 필터를 추가한다(JWT 인증 필터 다음에 실행된다)
        }
    }
}