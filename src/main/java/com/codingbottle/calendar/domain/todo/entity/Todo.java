package com.codingbottle.calendar.domain.todo.entity;

import com.codingbottle.calendar.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean isChecked;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "todo_tag_id", nullable = false)
    private TodoTag todoTag;

    @Builder
    private Todo(Long id, Member member, String title, LocalDate date, TodoTag todoTag) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.date = date;
        this.todoTag = todoTag;
        this.isChecked = false;
    }

    public Member getId() {
        return member;
    }

    public void toggleCheckStatus() {
        this.isChecked = !isChecked;
    }

    public void updateTitle(String titleToUpdate) {
        this.title = titleToUpdate;
    }



}
