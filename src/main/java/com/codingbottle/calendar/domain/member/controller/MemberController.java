package com.codingbottle.calendar.domain.member.controller;

import com.codingbottle.calendar.domain.member.dto.MemberPostDto;
import com.codingbottle.calendar.domain.member.dto.MemberResponseDto;
import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.mapper.MemberMapper;
import com.codingbottle.calendar.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;

    public MemberController(MemberService memberService, MemberMapper memberMapper) {
        this.memberService = memberService;
        this.memberMapper = memberMapper;
    }

    // 회원가입
    @PostMapping
    public ResponseEntity<MemberResponseDto> postMember(@Valid @RequestBody MemberPostDto memberPostDto) {
        MemberResponseDto memberResponseDto = memberService.createMember(memberPostDto);

        return new ResponseEntity<>(memberResponseDto, HttpStatus.CREATED);
    }

    // 로그인한 Member 정보 반환
    @GetMapping
    public ResponseEntity<MemberResponseDto> getMember(@AuthenticationPrincipal Member member) {
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(member);

        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }
}