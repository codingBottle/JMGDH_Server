package com.codingbottle.calendar.domain.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TagListRspDto {

    private Long id;
    private String tagName;
    private String color;
}
