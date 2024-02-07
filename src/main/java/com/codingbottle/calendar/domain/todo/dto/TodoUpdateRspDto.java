package com.codingbottle.calendar.domain.todo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


public record TodoUpdateRspDto(
        @NotBlank(message = "할 일을 입력해주세요.")
        String title,
        @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
        @NotNull
        LocalDate date,
        @NotNull
        String tagName,
        @NotNull
        String color,
        boolean isChecked
) {
}