package com.codingbottle.calendar.domain.todo.controller;

import com.codingbottle.calendar.domain.todo.dto.*;
import com.codingbottle.calendar.domain.todo.entity.Todo;
import com.codingbottle.calendar.domain.todo.service.TodoService;
import com.codingbottle.calendar.global.api.RspTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class TodoController {

    private final TodoService todoService;

    // 투두 등록
    @PostMapping("/todos")
    public ResponseEntity<RspTemplate<Void>> handleCreate(
            @RequestBody @Valid TodoCreateReqDto reqDto,
            Authentication authentication
    ) {
        todoService.create(reqDto, Long.parseLong(authentication.getName()));

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.CREATED,
                reqDto.date().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        + " 할 일 생성");
        return ResponseEntity.status(HttpStatus.CREATED).body(rspTemplate);
    }

    // 투두 수정
    @PatchMapping("/todos/{todoId}/title")
    public ResponseEntity<RspTemplate<Void>> handleUpdate(
            @PathVariable Long todoId,
            @RequestBody @Valid TodoUpdateRspDto reqDto,
            Authentication authentication
    ) {
        todoService.update(todoId, reqDto, Long.parseLong(authentication.getName()));

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK,
                "할 일이 수정되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(rspTemplate);
    }

    // 투두 체크 여부 수정
    @PatchMapping("/todos/{todoId}/check")
    public ResponseEntity<RspTemplate<Void>> handleUpdateCheckStatus(
            @PathVariable Long todoId,
            @RequestParam boolean newCheck,
            Authentication authentication
    ) {
        long memberId = Long.parseLong(authentication.getName());
        todoService.updateCheck(todoId, newCheck, memberId);

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK,
                "할 일 체크 여부 수정 완료");
        return ResponseEntity.status(HttpStatus.OK).body(rspTemplate);
    }

    // 투두 삭제
    @DeleteMapping("/todos/{id}")
    public ResponseEntity<RspTemplate<Void>> handleDelete(
            @PathVariable Long id,
            Authentication authentication
    ) {
        todoService.delete(id, Long.parseLong(authentication.getName()));

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK,
                "할 일이 삭제되었습니다");
        return ResponseEntity.status(HttpStatus.OK).body(rspTemplate);
    }

    // 모든 투두 조회
    @GetMapping("/todos")
    public ResponseEntity<List<Todo>> handleGetAllTodos(Authentication authentication) {
        long memberId = Long.parseLong(authentication.getName());
        List<Todo> todos = todoService.getAllTodosByMemberId(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(todos);
    }


}

