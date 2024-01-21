package com.codingbottle.calendar.global.config.init;

import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class InitMember implements ApplicationRunner {
    private final MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String uuid = UUID.randomUUID().toString().substring(0, 10);
        Member member = Member.builder()
                .email(uuid)
                .password(uuid)
                .nickname(uuid)
                .googleAccessToken(uuid)
                .build();

        memberRepository.save(member);
    }
}
