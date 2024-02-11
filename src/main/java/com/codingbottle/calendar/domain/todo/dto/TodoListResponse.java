package com.codingbottle.calendar.domain.todo.dto;

import com.codingbottle.calendar.domain.todo.entity.TagColor;
import com.codingbottle.calendar.domain.todo.entity.TodoTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class TodoListResponse {
    List<TodoTagDto> todoTags;

    public static TodoListResponse from(List<TodoTag> todoTags, List<TodoResponseWithTagId> todos) {
        // todoTags를 순회하면서 각각의 todoTag에 해당하는 todo들을 찾아서 TodoTagDto를 생성
        List<TodoTagDto> todoTagDtoList = todoTags.stream()
                .map(todoTag -> {
                    List<TodoResponseWithTagId> todoList = todos.stream()
                            .filter(todo -> todoTag.getId().equals(todo.getTagId()))
                            .toList();
                    return TodoTagDto.from(todoTag, todoList);
                })
                .toList();

        return new TodoListResponse(todoTagDtoList);
    }

    @Getter
    @Builder
    private static class TodoTagDto {
        long id;
        String tagName;
        TagColor color;
        List<TodoDto> todoDtos;

        static TodoTagDto from(TodoTag todoTag, List<TodoResponseWithTagId> todos) {
            return TodoTagDto.builder()
                    .id(todoTag.getId())
                    .tagName(todoTag.getTagName())
                    .color(todoTag.getColor())
                    .todoDtos(todos.stream()
                            .map(TodoDto::from)
                            .toList())
                    .build();
        }
    }

    @Builder
    @Getter
    private static class TodoDto {
        long id;
        String title;
        boolean isChecked;

        static TodoDto from(TodoResponseWithTagId todo) {
            return TodoDto.builder()
                    .id(todo.getId())
                    .title(todo.getTitle())
                    .isChecked(todo.isChecked())
                    .build();
        }
    }


}
