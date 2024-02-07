package com.codingbottle.calendar.domain.todo.entity;

import com.codingbottle.calendar.domain.common.BaseTimeEntity;
import com.codingbottle.calendar.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class TodoTag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tagName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TagColor color;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public TodoTag(Long id, String tagName, TagColor color, Member member) {
        this.id = id;
        this.tagName = tagName;
        this.color = color;
        this.member = member;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public void setColor(TagColor color) {
        this.color = color;
    }

}
