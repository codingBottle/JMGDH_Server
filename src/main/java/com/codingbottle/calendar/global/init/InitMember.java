package com.codingbottle.calendar.global.init;

import com.codingbottle.calendar.domain.member.entity.Member;
import com.codingbottle.calendar.domain.member.repository.MemberRepository;
import com.codingbottle.calendar.domain.todo.entity.Todo;
import com.codingbottle.calendar.domain.todo.entity.TodoTag;
import com.codingbottle.calendar.domain.todo.repository.TodoRepository;
import com.codingbottle.calendar.domain.todo.repository.TodoTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class InitMember implements ApplicationRunner {
    private final MemberRepository memberRepository;
    private final TodoTagRepository todoTagRepository;
    private final TodoRepository todoRepository;

    @Transactional
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String uuid = UUID.randomUUID().toString().substring(0, 10);
        Member member = Member.builder()
                .email(uuid)
                .imageUrl(uuid)
                .nickname(uuid)
                .build();

        memberRepository.save(member);
        List<TodoTag> todoTags = TodoTag.generateInitialTags(member);

        todoTagRepository.saveAll(todoTags);

        List<Todo> list = IntStream.rangeClosed(1, 4)
                .mapToObj(i -> Todo.builder()
                            .title("투두" + i)
                            .date(LocalDate.of(2024, 2, 1))
                            .member(member)
                            .todoTag(todoTags.get(0))
                            .build())
                .toList();

        todoRepository.saveAll(list);
    }
}
