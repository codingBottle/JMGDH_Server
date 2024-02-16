package com.codingbottle.calendar.domain.todo.repository;

import com.codingbottle.calendar.domain.todo.dto.TodoResponseWithTagId;
import com.codingbottle.calendar.domain.todo.entity.Todo;
import com.codingbottle.calendar.domain.todo.entity.TodoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // todoId와 memberId를 기반으로 특정 투두 찾기
    Optional<Todo> findByIdAndMemberId(Long todoId, Long memberId);

    //멤버id로 모든 투두 조회
    List<Todo> findAllByMemberId(long memberId);

    @Query("SELECT new com.codingbottle.calendar.domain.todo.dto.TodoResponseWithTagId(t.id, t.title, t.isChecked, t.todoTag.id)" +
            " FROM Todo t" +
            " WHERE t.todoTag.id IN :tagIds" +
            " AND t.date = :date")
    List<TodoResponseWithTagId> findAllByTagAndDate(@Param("tagIds") List<Long> tagIds, @Param("date") LocalDate date);

    // 삭제량이 많아 SQL로 바로 삭제
    @Modifying
    @Query("DELETE FROM Todo t" +
            " WHERE t.todoTag = :todoTag")
    void deleteByTodoTag(@Param("todoTag") TodoTag todoTag);

}
