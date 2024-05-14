package com.dotori.dotori.controller;

import com.dotori.dotori.dto.TodoDTO;
import com.dotori.dotori.entity.Todo;
import com.dotori.dotori.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/create")
    public void createTodo(Model model){
        log.info("create todo");
    }

    @PostMapping
    public ResponseEntity<Integer> createTodo(@RequestBody TodoDTO todoDto) {
        int todo = todoService.createTodo(todoDto);
        return ResponseEntity.ok(todo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoDTO> getTodoById(@PathVariable int id) {
        TodoDTO todo = todoService.getTodoById(id);
        return ResponseEntity.ok(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updateTodo(@PathVariable int id, @RequestBody TodoDTO todoDto) {
        todoService.updateTodo(id, todoDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable int id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }


}
