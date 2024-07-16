package com.dotori.dotori.service;

import com.dotori.dotori.dto.TodoDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Log4j2
@SpringBootTest
public class TodoServiceTests {

    @Autowired
    private TodoService todoService;

    @Test
    public void testAddTodo() {
        TodoDTO todo = TodoDTO.builder()
                .content("testTodo")
                .aid(1)
                .build();

        int resultId = todoService.addTodo(todo);
        log.info(todo.toString());
    }

    @Test
    public void testReadOneTodo(){
        TodoDTO todo = todoService.readOneTodo(1);
        log.info(todo.toString());
    }

    @Test
    public void testGetAllTodo(){
        List<TodoDTO> todos = todoService.getAllTodo();
        todos.forEach(
                todo -> log.info(todo.toString())
        );
    }

    @Test
    public void testUpdateTodo(){
        TodoDTO todoDTO = TodoDTO.builder()
                .id(100)
                .category("공부")
                .content("Service test")
                .done(true).build();
        todoService.updateTodo(todoDTO);
    }

    @Test
    public void testDeleteTodo(){
        todoService.deleteTodo(101);
    }

}
