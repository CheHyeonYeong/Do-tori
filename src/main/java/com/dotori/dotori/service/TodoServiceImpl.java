package com.dotori.dotori.service;

import com.dotori.dotori.dto.TodoDTO;
import com.dotori.dotori.entity.Todo;
import com.dotori.dotori.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;

    @Override
    public int addTodo(TodoDTO todoDTO) {
        Todo todo = modelMapper.map(todoDTO, Todo.class);
        log.info("Add todo: " + todo);
        int id = todoRepository.save(todo).getId();
        return id;
    }

    @Override
    public TodoDTO readOneTodo(int id) {

        Optional<Todo> result = todoRepository.findById(id);
        Todo todo = result.orElseThrow();
        TodoDTO todoDTO = modelMapper.map(todo, TodoDTO.class);
        return todoDTO;
    }

    @Override
    public void updateTodo(TodoDTO todoDTO) {
        Optional<Todo> result = todoRepository.findById(todoDTO.getId());
        Todo todo = result.orElseThrow();
        todo.changeTodo(todoDTO.getCategory(), todoDTO.getContent(), todoDTO.isDone());
        todoRepository.save(todo);

        log.info("Todo Service : Todo updated " + todo, " TodoDTO : "+todoDTO);
    }

    @Override
    public void deleteTodo(int id) {
        todoRepository.deleteById(id);
    }
}
