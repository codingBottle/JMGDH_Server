package com.codingbottle.calendar.domain.todo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


public record TodoCreateReqDto(
        @NotBlank(message = "할 일을 입력해주세요.")
        String title,
        @JsonFormat(pattern = "yyyy-MM-dd", shape = Shape.STRING)
        @NotNull
        LocalDate date,
        long tagId
) {
}