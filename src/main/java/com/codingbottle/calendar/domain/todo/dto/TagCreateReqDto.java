package com.codingbottle.calendar.domain.todo.dto;

import com.codingbottle.calendar.domain.todo.entity.TagColor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record TagCreateReqDto (
        @NotBlank(message = "태그 이름을 입력해주세요.")
        String tagName,
        @NotNull(message = "태그 색상을 선택해주세요.")
        TagColor color
) {
}
