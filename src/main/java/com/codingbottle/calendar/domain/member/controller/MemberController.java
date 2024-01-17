package com.codingbottle.calendar.domain.member.controller;

import com.codingbottle.calendar.domain.member.dto.MemberResponseDto;
import com.codingbottle.calendar.domain.member.mapper.MemberMapper;
import com.codingbottle.calendar.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final OAuth2AuthorizedClientService authorizedClientService;

    public MemberController(MemberService memberService, MemberMapper memberMapper, OAuth2AuthorizedClientService authorizedClientService) {
        this.memberService = memberService;
        this.memberMapper = memberMapper;
        this.authorizedClientService = authorizedClientService;
    }

    // 로그인한 Member 정보 반환
    @GetMapping
    public ResponseEntity<MemberResponseDto> getMember(@AuthenticationPrincipal long memberId) {
        MemberResponseDto memberResponseDto = memberMapper.memberToMemberResponseDto(memberService.getById(memberId));

        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }
}