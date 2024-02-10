package com.codingbottle.calendar.domain.todo.service;

import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.repository.MemberRepository;
import com.codingbottle.calendar.domain.todo.dto.TodoCreateReqDto;
import com.codingbottle.calendar.domain.todo.dto.TodoUpdateReqDto;
import com.codingbottle.calendar.domain.todo.entity.Todo;
import com.codingbottle.calendar.domain.todo.entity.TodoTag;
import com.codingbottle.calendar.domain.todo.repository.TodoRepository;
import com.codingbottle.calendar.domain.todo.repository.TodoTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final TodoTagRepository todoTagRepository;
    private final MemberRepository memberRepository;

    //투두 내용 추가
    @Transactional
    public void create(TodoCreateReqDto reqDto, long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("ID가 " + memberId + "인 회원을 찾을 수 없습니다."));

        TodoTag todoTag = todoTagRepository.findById(reqDto.tagId())
                .orElseThrow(() -> new IllegalArgumentException("ID가 " + reqDto.tagId() + "인 태그를 찾을 수 없습니다."));

        Todo todo = Todo.builder()
                .title(reqDto.title())
                .date(reqDto.date())
                .member(member)
                .todoTag(todoTag)
                .build();

        todoRepository.save(todo);
    }

    // 내용 수정
    @Transactional
    public void update(long todoId, TodoUpdateReqDto reqDto, long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("ID가 " + memberId + "인 회원을 찾을 수 없습니다."));

        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("ID가 " + todoId + "인 Todo를 찾을 수 없습니다."));
        validateTodoOwnership(todo, member);

        todo.updateTitle(reqDto.title());
    }
    // 본인 확인
    private void validateTodoOwnership(Todo todo, Member member) {
        if (!todo.getMember().getId().equals(member.getId())) {
            throw new IllegalArgumentException("ID가"+ todo.getId() + "인 할 일은 현재 로그인한 회원에게 속하지 않습니다.");
        }
    }

    // 체크 여부 수정
    @Transactional
    public void updateCheck(Long todoId, boolean newCheck, Long memberId) {
        Todo todo = findTodoByIdAndMember(todoId, memberId);
        todo.setCheck(newCheck);
    }

    // todo를 확인
    private Todo findTodoByIdAndMember(Long todoId, Long memberId) {
        return (Todo) todoRepository.findByIdAndMemberId(todoId, memberId)
                .orElseThrow(() -> new IllegalArgumentException("ID가 " + todoId + "인 Todo를 찾을 수 없습니다."));
    }

    // 내용 삭제
    @Transactional
    public void delete(long todoId, long memberId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new NoSuchElementException("ID가 " + todoId + "인 Todo를 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("ID가 " + memberId + "인 회원을 찾을 수 없습니다."));

        validateTodoOwnership(todo, member);
        todoRepository.delete(todo);
    }

    // 모든 투두 조회
    public List<Todo> getAllTodosByMemberId(long memberId) {
        return todoRepository.findAllByMemberId(memberId);
    }

}
