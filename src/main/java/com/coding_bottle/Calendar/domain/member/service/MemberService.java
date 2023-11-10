package com.coding_bottle.Calendar.domain.member.service;

import com.coding_bottle.Calendar.domain.member.dto.MemberPostDto;
import com.coding_bottle.Calendar.domain.member.dto.MemberResponseDto;
import com.coding_bottle.Calendar.domain.member.entity.Member;
import com.coding_bottle.Calendar.domain.member.mapper.MemberMapper;
import com.coding_bottle.Calendar.domain.member.repository.MemberRepository;
import com.coding_bottle.Calendar.global.utils.CustomAuthorityUtils;
import org.springframework.dao.DataIntegrityViolationException;
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

    // 이메일 중복여부 확인
    public void verifyExistsEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent())
            throw new DataIntegrityViolationException("이미 존재하는 이메일입니다.");
    }
}