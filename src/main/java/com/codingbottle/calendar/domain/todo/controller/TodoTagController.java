package com.codingbottle.calendar.domain.todo.controller;

import com.codingbottle.calendar.domain.todo.dto.TagCreateReqDto;
import com.codingbottle.calendar.domain.todo.entity.TodoTag;
import com.codingbottle.calendar.domain.todo.service.TodoTagService;
import com.codingbottle.calendar.global.api.RspTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class TodoTagController {

    private final TodoTagService todoTagService;
    private final TodoTagService tagService;

    // 태그 추가
    @PostMapping("/tags")
    public ResponseEntity<RspTemplate<Void>> createTag(
            @RequestBody @Valid TagCreateReqDto reqDto
            , Authentication authentication
    ) {
        tagService.createTag(reqDto, Long.parseLong(authentication.getName()));
        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.CREATED, "새로운 태그 생성");
        return ResponseEntity.status(HttpStatus.CREATED).body(rspTemplate);
    }

    // 태그 수정
    @PatchMapping("/tags/{tagId}")
    public ResponseEntity<RspTemplate<Void>> handleUpdateTag(
            @PathVariable Long tagId,
            @RequestBody @Valid TagCreateReqDto reqDto
    ) {
        tagService.updateTag(tagId, reqDto);

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK, "태그가 수정되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(rspTemplate);
    }

    // 태그 삭제
    @DeleteMapping("/tags/{tagId}")
    public ResponseEntity<RspTemplate<Void>> handleDeleteTag(@PathVariable Long tagId) {
        tagService.deleteTag(tagId);

        RspTemplate<Void> rspTemplate = new RspTemplate<>(HttpStatus.OK, "태그가 삭제되었습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(rspTemplate);
    }

    // 회원 ID로 모든 태그 조회
    @GetMapping("/tags")
    public ResponseEntity<List<TodoTag>> handleGetAllTags(Authentication authentication) {
        long memberId = Long.parseLong(authentication.getName());
        List<TodoTag> tags = todoTagService.getAllTagsByMemberId(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(tags);
    }
}
