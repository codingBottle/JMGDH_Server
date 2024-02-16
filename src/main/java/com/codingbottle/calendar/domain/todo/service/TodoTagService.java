package com.codingbottle.calendar.domain.todo.service;

import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.repository.MemberRepository;
import com.codingbottle.calendar.domain.todo.dto.TagCreateReqDto;
import com.codingbottle.calendar.domain.todo.entity.TodoTag;
import com.codingbottle.calendar.domain.todo.repository.TodoRepository;
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
    private final MemberRepository memberRepository;
    private final TodoRepository todoRepository;

    // 새로운 태그 등록
    @Transactional
    public void createTag(TagCreateReqDto reqDto, long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 회원을 찾을 수 없습니다: " + memberId));

        TodoTag todoTag = TodoTag.builder()
                .tagName(reqDto.tagName())
                .color(reqDto.tagColor())
                .member(member)
                .build();

        todoTagRepository.save(todoTag);
    }

    // 태그 속성 수정
    @Transactional
    public void updateTag(Long tagId, TagCreateReqDto reqDto) {
        TodoTag todoTag = todoTagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found with id: " + tagId));

        todoTag.update(reqDto.tagName(), reqDto.tagColor());
    }

    // 태그 속성 삭제
    @Transactional
    public void deleteTag(long tagId, long memberId) {
        TodoTag todoTag = todoTagRepository.findByIdFetchMember(tagId)
                .orElseThrow(() -> new IllegalArgumentException("ID에 해당하는 태그를 찾을 수 없습니다: " + tagId));
        if (todoTag.getMember().getId() != memberId) {
            throw new IllegalArgumentException("태그를 삭제할 권한이 없습니다.");
        }
        todoRepository.deleteByTodoTag(todoTag);

        todoTagRepository.delete(todoTag);
    }

    // 회원 ID로 모든 태그 조회
    public List<TodoTag> getAllTagsByMemberId(long memberId) {
        return todoTagRepository.findAllByMemberId(memberId);
    }

}
