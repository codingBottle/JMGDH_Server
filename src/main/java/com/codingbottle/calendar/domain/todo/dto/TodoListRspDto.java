package com.codingbottle.calendar.domain.todo.dto;

import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.todo.entity.Todo;
import com.codingbottle.calendar.domain.todo.entity.TodoTag;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class TodoListRspDto {
    List<TodoDto> todos;

    @Getter
    static class TodoDto {
        Member id;
        String title;
        LocalDate date;
        boolean isChecked;
        TodoTag todoTag;

        TodoDto(Todo todo) {
            this.id = todo.getId();
            this.title = todo.getTitle();
            this.date = todo.getDate();
            this.isChecked = todo.isChecked();
            this.todoTag = todo.getTodoTag();
        }

    }
}
