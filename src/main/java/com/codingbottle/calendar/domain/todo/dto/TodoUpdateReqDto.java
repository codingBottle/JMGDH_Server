package com.codingbottle.calendar.domain.todo.dto;

import javax.validation.constraints.NotBlank;


public record TodoUpdateReqDto(
        @NotBlank(message = "할 일을 입력해주세요.")
        String title
) {
}