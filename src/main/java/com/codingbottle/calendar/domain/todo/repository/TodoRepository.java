package com.codingbottle.calendar.domain.todo.repository;

import com.codingbottle.calendar.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // todoId와 memberId를 기반으로 특정 투두 찾기
    Optional<Todo> findByIdAndMemberId(Long todoId, Long memberId);

    //멤버id로 모든 투두 조회
    List<Todo> findAllByMemberId(long memberId);
}
