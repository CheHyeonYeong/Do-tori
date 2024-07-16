package com.dotori.dotori.repository;

import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.entity.Todo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Log4j2
@SpringBootTest
public class TodoRepositoryTests {
    // CRUD Test를 위함

    @Autowired
    private TodoRepository todoRepository;

    // Create
    @Test
    public void testInsert() {
        IntStream.range(1,10).forEach(i -> {
            Todo todo = Todo.builder()
                    .content("title.............."+i)
                    .aid(i%10)
                    .done(false)
                    .todoDate(LocalDate.now())
                    .build();
            todoRepository.save(todo);
        });
    }

    // Read
    @Test
    public void testSelect() {
        int id = 10;

        Optional<Todo> result = todoRepository.findById(id);  //optional Type으로 받아서 처리해야 함
        Todo todo = result.orElseThrow();
        log.info(todo);
    }

    // Read2 - find all
    @Test
    public void testFindAll(){
        List<Todo> todos = todoRepository.findAll();
        todos.forEach(todo -> {
            log.info(todo.toString());
        } );
    }

    // Update
    @Test
    public void testUpdate(){
        int id = 1;
        Optional<Todo> result = todoRepository.findById(id);
        Todo todo = result.orElseThrow();
        LocalDate updateTodoDate = LocalDate.now();

        todo.changeTodo("update category", "update content", true, updateTodoDate);
        todoRepository.save(todo);
    }

    // Delete
    @Test
    public void testDelete() {
        int id = 90;
        Optional<Todo> result = todoRepository.findById(id);
        Todo todo = result.orElseThrow();
        todoRepository.delete(todo);
    }

}
