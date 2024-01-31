package com.codingbottle.calendar.domain.Team.dto;

import javax.validation.constraints.NotBlank;

public record TeamUpdateReqDto(
        @NotBlank
        Long id,
        @NotBlank
        String name
) {
}
