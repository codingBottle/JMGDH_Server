package com.codingbottle.calendar.domain.todo.entity;

import com.codingbottle.calendar.domain.common.BaseTimeEntity;
import com.codingbottle.calendar.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public TodoTag(Long id, String tagName, TagColor color, Member member) {
        this.id = id;
        this.tagName = tagName;
        this.color = color;
        this.member = member;
    }

    public void update(String name, TagColor color) {
        this.tagName = name;
        this.color = color;
    }

    public static List<TodoTag> generateInitialTags(Member member) {
        return List.of(
                TodoTag.builder().tagName("문화생활").color(TagColor.BLUSH_PINK).member(member).build(),
                TodoTag.builder().tagName("학교").color(TagColor.CREAMY_PEACH).member(member).build(),
                TodoTag.builder().tagName("친구").color(TagColor.VANILLA_CREAM).member(member).build(),
                TodoTag.builder().tagName("알바").color(TagColor.MINT_GREEN).member(member).build(),
                TodoTag.builder().tagName("동아리").color(TagColor.LIGHT_KHAKI).member(member).build()
        );
    }
}
