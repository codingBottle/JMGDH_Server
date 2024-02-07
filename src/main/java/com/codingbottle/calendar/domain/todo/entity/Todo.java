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
    @JoinColumn(name = "tag_id", nullable = false)
    private TodoTag todoTag;

    @Builder
    public Todo(Long id, Member member, String title, boolean isChecked, LocalDate date, TodoTag todoTag) {
        this.id = id;
        this.member = member;
        this.title = title;
        this.isChecked = isChecked;
        this.date = date;
        this.todoTag = todoTag;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public Member getId() {
        return member;
    }

    public void setCheck(boolean newCheck) {
        this.isChecked = newCheck;
    }

    private void setDate(LocalDate date) {
        this.date = date;
    }

    public void updateFrom(Todo updatedTodo) {
        if (updatedTodo.getTitle() != null) {
            this.setTitle(updatedTodo.getTitle());
        }
        if (updatedTodo.getDate() != null) {
            this.setDate(updatedTodo.getDate());
        }
        this.setCheck(updatedTodo.isChecked());
    }



}
