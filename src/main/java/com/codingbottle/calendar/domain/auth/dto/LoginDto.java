package com.codingbottle.calendar.domain.auth.dto;

public record LoginDto(
        String username,
        String password
) {
}