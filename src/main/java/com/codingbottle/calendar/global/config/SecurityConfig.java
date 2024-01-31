package com.codingbottle.calendar.global.config;

import com.codingbottle.calendar.domain.auth.filter.JwtVerificationFilter;
import com.codingbottle.calendar.domain.auth.handler.MemberAccessDeniedHandler;
import com.codingbottle.calendar.domain.auth.handler.MemberAuthenticationEntryPoint;
import com.codingbottle.calendar.domain.auth.handler.OAuth2MemberFailureHandler;
import com.codingbottle.calendar.domain.auth.handler.OAuth2MemberSuccessHandler;
import com.codingbottle.calendar.domain.auth.jwt.JwtTokenizer;
import com.codingbottle.calendar.global.utils.CustomAuthorityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final OAuth2MemberSuccessHandler oAuth2MemberSuccessHandler;
    private final OAuth2MemberFailureHandler oAuth2MemberFailureHandler;
    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.clientSecret}")
    private String clientSecret;

    // @Lazy는 빈 객체끼리 순환 참조를 막기 위해 사용
    public SecurityConfig(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils
            , OAuth2MemberSuccessHandler oAuth2MemberSuccessHandler, OAuth2MemberFailureHandler oAuth2MemberFailureHandler) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.oAuth2MemberSuccessHandler = oAuth2MemberSuccessHandler;
        this.oAuth2MemberFailureHandler = oAuth2MemberFailureHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())    // cors 설정
                .and()
                .csrf().disable()          // Csrf 비활성화
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
                        .anyRequest().permitAll() // 모든 접근 비허용 후 화이트리스트 기반 인증
                )
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2MemberSuccessHandler)
                        .failureHandler(oAuth2MemberFailureHandler))
        ;
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
                "http://localhost:8080"                             // 모든 경로 CORS 허용(추후 Client URL 허용)
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
            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, authorityUtils); // JWT 검증 필터 선언

            builder.addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class); // 필터에 JWT 검증 필터를 추가한다(JWT 인증 필터 다음에 실행된다)
        }
    }
    // yaml 설정으로 대체
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        var clientRegistration = clientRegistration();
//
//        return new InMemoryClientRegistrationRepository(clientRegistration);
//    }
//
//    private ClientRegistration clientRegistration() {
//
//        return CommonOAuth2Provider
//                .GOOGLE
//                .getBuilder("google")
//                .clientId(clientId)
//                .clientSecret(clientSecret)
//                .scope("email", "profile", "https://www.googleapis.com/auth/calendar")
//                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth?access_type=offline")
//                .build();
//    }
}