package com.codingbottle.calendar.domain.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoResponseWithTagId {
    long id;
    String title;
    boolean isChecked;
    long tagId;
}
