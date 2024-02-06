package com.codingbottle.calendar.domain.todo.service;

import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.todo.dto.TagCreateReqDto;
import com.codingbottle.calendar.domain.todo.dto.TagUpdateReqDto;
import com.codingbottle.calendar.domain.todo.entity.TodoTag;
import com.codingbottle.calendar.domain.todo.repository.TodoTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TodoTagService {

    private final TodoTagRepository todoTagRepository;

    // 새로운 태그 등록
    @Transactional
    public void createTag(TagCreateReqDto reqDto, long memberId) {
        Member member = Member.createMember(memberId);

        TodoTag todoTag = TodoTag.builder()
                .tagName(reqDto.tagName())
                .color(reqDto.color())
                .member(member)
                .build();

        todoTagRepository.save(todoTag);
    }

    // 태그 속성 수정
    @Transactional
    public void updateTag(Long tagId, TagUpdateReqDto reqDto) {
        TodoTag todoTag = todoTagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found with id: " + tagId));

        if (reqDto.getTagName() != null) {
            todoTag.setTagName(reqDto.getTagName());
        }

        if (reqDto.getColor() != null) {
            todoTag.setColor(reqDto.getColor());
        }

        todoTagRepository.save(todoTag);
    }

    // 태그 속성 삭제
    @Transactional
    public void deleteTag(Long tagId) {
        TodoTag todoTag = todoTagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 태그를 찾을 수 없습니다: " + tagId));

        todoTagRepository.delete(todoTag);
    }

    // 회원 ID로 모든 태그 조회
    public List<TodoTag> getAllTagsByMemberId(long memberId) {
        return todoTagRepository.findAllByMemberId(memberId);
    }

}
