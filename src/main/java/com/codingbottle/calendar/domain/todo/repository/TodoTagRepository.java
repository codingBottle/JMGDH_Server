package com.codingbottle.calendar.domain.todo.repository;

import com.codingbottle.calendar.domain.todo.entity.TodoTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TodoTagRepository extends JpaRepository<TodoTag, Long> {

    // TodoTag에 해당 tagName의 존재여부 찾기
    Optional<TodoTag> findByTagName(String tagName);

    // 멤버id에 해당하는 모든 태그 찾기
    @Query("SELECT t FROM TodoTag t WHERE t.member.id = :memberId")
    List<TodoTag> findAllByMemberId(@Param("memberId") long memberId);

    @Query("SELECT t FROM TodoTag t JOIN FETCH t.member WHERE t.id = :tagId")
    Optional<TodoTag> findByIdFetchMember(@Param("tagId") Long tagId);
}
