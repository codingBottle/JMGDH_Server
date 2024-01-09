package com.codingbottle.calendar.domain.member.service;

import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.mapper.MemberMapper;
import com.codingbottle.calendar.domain.member.repository.MemberRepository;
import com.codingbottle.calendar.global.exception.common.BusinessException;
import com.codingbottle.calendar.global.exception.common.ErrorCode;
import com.codingbottle.calendar.global.utils.CustomAuthorityUtils;
import com.codingbottle.calendar.global.utils.RandomPasswordGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils customAuthorityUtils;
    private final MemberMapper memberMapper;
    private final RandomPasswordGenerator randomPasswordGenerator;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, CustomAuthorityUtils customAuthorityUtils, MemberMapper memberMapper, RandomPasswordGenerator randomPasswordGenerator) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.customAuthorityUtils = customAuthorityUtils;
        this.memberMapper = memberMapper;
        this.randomPasswordGenerator = randomPasswordGenerator;
    }

    // 회원가입
    @Transactional
    public Member createMember(String email, String password, String nickname, String accessToken) {

        verifyExistsEmail(email); // 이메일 중복 여부 확인

        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password)) // 비밀번호 암호화
                .nickname(nickname)
                .role(customAuthorityUtils.createUserRoles(email))
                .googleAccessToken(accessToken)
                .build();

        member = memberRepository.save(member);

        return member;
    }

    @Transactional
    public Member updateMember(Member member, String accessToken) {

        Member updateMember = Member.builder()
                .memberId(member.getMemberId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPassword())
                .role(member.getRole())
                .googleAccessToken(accessToken)
                .build();

        memberRepository.save(updateMember);

        return updateMember;
    }

    @Transactional
    public Optional<Member> getMemberByEmail(String email){
        Optional<Member> member = memberRepository.findByEmail(email);

        return member;
    }

    // 이메일 중복여부 확인
    public void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent())
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);

    }

    @Transactional
    // member가 존재하면 member 반환, 없으면 예외 처리
    public Member existsMemberById(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isEmpty())
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        else
            return member.get();
    }

    public Member getById(long id) {
        return memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    // 자체 회원가입 된 멤버, 가입이 안된 멤버 컨트롤 하는 로직
    @Transactional
    public Member oAuth2CheckMember(String email, String nickname, String accessToken) {
        Optional<Member> optionalMember = getMemberByEmail(email);
        Member member;

        // 가입이 안되어 있으면 회원가입
        if(optionalMember.isEmpty()) {
            String password = randomPasswordGenerator.generateRandomPassword();
            member = createMember(email, password, nickname, accessToken);
        }
        // 가입 되어있으면 로그인
        else {
            member = optionalMember.get();
            member = updateMember(member, accessToken);
        }

        return member;
    }
}