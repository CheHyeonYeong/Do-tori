package com.dotori.dotori.repository;

import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.entity.Todo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
public class TodoRepositoryTests {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void testInsert() {
        IntStream.range(1,100).forEach(i -> {
            Todo todo = Todo.builder()
                    .content("title.............."+i)
                    .aid(i%10)
                    .done(false)
                    .build();
            todoRepository.save(todo);
        });
    }

    @Test
    public void testSelct() {
        int id = 10;

        Optional<Todo> result = todoRepository.findById(id);  //optional Type으로 받아서 처리해야 함
        Todo todo = result.orElseThrow();
        log.info(todo);
    }



}
