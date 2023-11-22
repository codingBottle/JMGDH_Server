package com.codingbottle.calendar.domain.member.service;

import com.codingbottle.calendar.domain.member.dto.MemberPostDto;
import com.codingbottle.calendar.domain.member.dto.MemberResponseDto;
import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.mapper.MemberMapper;
import com.codingbottle.calendar.domain.member.repository.MemberRepository;
import com.codingbottle.calendar.global.exception.common.BusinessException;
import com.codingbottle.calendar.global.exception.common.ErrorCode;
import com.codingbottle.calendar.global.utils.CustomAuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils customAuthorityUtils;
    private final MemberMapper memberMapper;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, CustomAuthorityUtils customAuthorityUtils, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.customAuthorityUtils = customAuthorityUtils;
        this.memberMapper = memberMapper;
    }

    // 회원가입
    @Transactional
    public MemberResponseDto createMember(MemberPostDto memberPostDto) {

        verifyExistsEmail(memberPostDto.email()); // 이메일 중복 여부 확인

        Member member = Member.builder()
                .email(memberPostDto.email())
                .password(passwordEncoder.encode(memberPostDto.password())) // 비밀번호 암호화
                .nickname(memberPostDto.nickname())
                .role(customAuthorityUtils.createUserRoles(memberPostDto.email()))
                .build();

        member = memberRepository.save(member);

        return memberMapper.memberToMemberResponseDto(member);
    }

    @Transactional
    public Member findMember(Long memberId) {
        Member member = existsMemberById(memberId);

        return member;
    }

    // 이메일 중복여부 확인
    public void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent())
            throw new BusinessException(ErrorCode.EMAIL_DUPLICATION);
    }

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
}