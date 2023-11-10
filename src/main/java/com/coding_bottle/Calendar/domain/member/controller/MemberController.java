package com.coding_bottle.Calendar.domain.member.controller;

import com.coding_bottle.Calendar.domain.member.dto.MemberPostDto;
import com.coding_bottle.Calendar.domain.member.dto.MemberResponseDto;
import com.coding_bottle.Calendar.domain.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 회원가입
    @PostMapping
    public ResponseEntity<MemberResponseDto> postMember(@Valid @RequestBody MemberPostDto memberPostDto) {
        MemberResponseDto memberResponseDto = memberService.createMember(memberPostDto);

        return new ResponseEntity<>(memberResponseDto, HttpStatus.CREATED);
    }
}