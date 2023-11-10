package com.coding_bottle.Calendar.domain.member.dto;

public record MemberResponseDto(
        Long memberId,
        String email,
        String nickname
) {
}