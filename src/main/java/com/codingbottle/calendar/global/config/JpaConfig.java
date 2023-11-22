package com.codingbottle.calendar.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @Component
    @RequiredArgsConstructor
    static class AuditorAwareImpl implements AuditorAware<String> {

        @Override
        public Optional<String> getCurrentAuditor() {
            // SecurityContext 의 Principal 을 String형식으로 저장
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
                return Optional.of("anonymous");
            }

            return Optional.of(authentication.getPrincipal().toString());
        }
    }
}
