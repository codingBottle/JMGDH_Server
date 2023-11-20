package com.codingbottle.calendar.global.config;

import com.codingbottle.calendar.domain.auth.handler.MemberAuthenticationFailureHandler;
import com.codingbottle.calendar.domain.auth.handler.MemberAuthenticationSuccessHandler;
import com.codingbottle.calendar.domain.auth.filter.JwtAuthenticationFilter;
import com.codingbottle.calendar.domain.auth.jwt.JwtTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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

    public SecurityConfig(JwtTokenizer jwtTokenizer) {
        this.jwtTokenizer = jwtTokenizer;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())    // cors 설정
                .and()
                .csrf().disable()        // Csrf 비활성화
                .formLogin().disable()   // Form login 비활성화
                .httpBasic().disable()   // Header에 로그인 정보를 담는 방식 비활성화
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()                // 모든 접근 허용
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

            builder.addFilter(jwtAuthenticationFilter); // 필터에 JWT 인증 필터를 추가한다
        }
    }
}