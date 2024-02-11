package com.codingbottle.calendar.domain.todo.dto;

import com.codingbottle.calendar.domain.todo.entity.TagColor;
import com.codingbottle.calendar.domain.todo.entity.TodoTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class TodoTagListResponse {

    List<TodoTagDto> todoTags;

    public static TodoTagListResponse from(List<TodoTag> todoTags) {
        List<TodoTagDto> todoTagDtoList = todoTags.stream()
                .map(todoTag -> TodoTagDto.builder()
                        .id(todoTag.getId())
                        .tagName(todoTag.getTagName())
                        .color(todoTag.getColor())
                        .build())
                .toList();
        return new TodoTagListResponse(todoTagDtoList);
    }

    @Builder
    @Getter
    private static class TodoTagDto {
        long id;
        String tagName;
        TagColor color;
    }

}
