package com.codingbottle.calendar.domain.member.dto;

public record MemberResponseDto(
        Long id,
        String email,
        String nickname
) {
}