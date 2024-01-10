package com.codingbottle.calendar.domain.member.dto;

public record MemberResponseDto(
        Long memberId,
        String email,
        String nickname,
        String googleAccessToken
) {
}