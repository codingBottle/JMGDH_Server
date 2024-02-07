package com.codingbottle.calendar.domain.todo.dto;

import com.codingbottle.calendar.domain.todo.entity.TagColor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagUpdateReqDto {

    private String tagName;
    private TagColor color;
}
