package com.codingbottle.calendar.domain.team.dto;

import javax.validation.constraints.NotBlank;

public record TeamCreateReqDto(
        @NotBlank(message = "팀명을 입력해주세요")
        String name
) {
}
