package com.codingbottle.calendar.domain.Team.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record TeamUpdateReqDto(
        @NotNull
        Long id,
        @NotBlank
        String name
) {
}
