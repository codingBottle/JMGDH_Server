package com.codingbottle.calendar.domain.member.controller;

import com.codingbottle.calendar.domain.member.dto.MemberResponseDto;
import com.codingbottle.calendar.domain.member.mapper.MemberMapper;
import com.codingbottle.calendar.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    public MemberController(MemberService memberService, MemberMapper memberMapper) {
        this.memberService = memberService;
        this.memberMapper = memberMapper;
    }

    // 로그인한 Member 정보 반환
    @GetMapping
    public ResponseEntity<MemberResponseDto> getMember(@AuthenticationPrincipal long memberId) {
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(memberService.getById(memberId));
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }

    // 탈퇴
    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@AuthenticationPrincipal long memberId) {
        memberService.deleteMember(memberId);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
}