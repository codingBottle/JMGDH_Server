package com.codingbottle.calendar.domain.member.service;

import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.repository.MemberRepository;
import com.codingbottle.calendar.domain.schedule.entity.CalendarApiIntegration;
import com.codingbottle.calendar.global.exception.common.BusinessException;
import com.codingbottle.calendar.global.exception.common.ErrorCode;
import com.codingbottle.calendar.global.utils.CustomAuthorityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils customAuthorityUtils;

    public MemberService(MemberRepository memberRepository, CustomAuthorityUtils customAuthorityUtils) {
        this.memberRepository = memberRepository;
        this.customAuthorityUtils = customAuthorityUtils;

    }

    // 회원가입
    @Transactional
    public Member createMember(String email, String nickname, String imageUrl) {

        verifyExistsEmail(email); // 이메일 중복 여부 확인

        Member member = Member.builder()
                .email(email)
                .nickname(nickname)
                .imageUrl(imageUrl)
                .role(customAuthorityUtils.createUserRoles(email))
                .build();

        CalendarApiIntegration calendarApiIntegration = CalendarApiIntegration.builder()
                .member(member)
                .build();

        member.setCalendarApiIntegration(calendarApiIntegration);
        member = memberRepository.save(member);

        return member;
    }

    @Transactional
    public Member updateMember(Member member, String nickname, String imageUrl) {

        Member updateMember = Member.builder()
                .id(member.getId())
                .nickname(nickname)
                .imageUrl(imageUrl)
                .email(member.getEmail())
                .role(member.getRole())
                .build();

        memberRepository.save(updateMember);

        return updateMember;
    }

    public Optional<Member> getOptionalMemberByEmail(String email){
        Optional<Member> member = memberRepository.findByEmail(email);

        return member;
    }

    public Member getMemberByEmail(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

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

    // 자체 회원가입 된 멤버, 가입이 안된 멤버 컨트롤 하는 로직
    @Transactional
    public Member oAuth2CheckMember(String email, String nickname, String imageUrl) {
        Optional<Member> optionalMember = getOptionalMemberByEmail(email);
        Member member;

        // 가입이 안되어 있으면 회원가입
        if(optionalMember.isEmpty()) {
            member = createMember(email, nickname, imageUrl);
        }
        // 가입 되어있으면 로그인
        else {
            member = optionalMember.get();

            if(!checkMember(member, nickname, imageUrl))
                member = updateMember(member, nickname, imageUrl);

        }

        return member;
    }

    @Transactional
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    private Boolean checkMember(Member member, String nickname, String imageUrl) {
        if(member.getNickname() != nickname || member.getImageUrl() != imageUrl)
            return false;
        else
            return true;
    }
}