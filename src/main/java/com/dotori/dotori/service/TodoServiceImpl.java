package com.dotori.dotori.service;

import com.dotori.dotori.dto.TodoDTO;
import com.dotori.dotori.entity.Todo;
import com.dotori.dotori.repository.TodoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class TodoServiceImpl implements TodoService{

    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;

    @Override
    public int createTodo(TodoDTO todoDTO) {
        Todo todo = Todo.builder()
                .category(todoDTO.getCategory())
                .content(todoDTO.getContent())
                .done(todoDTO.isDone())
                .build();
        return todoRepository.save(todo).getId();
    }

//    @Override
//    public List<TodoDTO> getAllTodo(LocalDateTime selectedDate) {
//        // 현재 로그인한 사용자와 선택한 날짜에 맞는 Todo 목록 가져오기
//        List<Todo> todos = todoRepository.findByAidAndDate(selectedDate);
//
//        // Todo를 TodoDTO로 변환
//        List<TodoDTO> todoDTO = todos.stream().map(todo -> modelMapper.map(todo, TodoDTO.class)).collect(Collectors.toList());
//        return todoDTO;
//    }

    @Override
    public TodoDTO getTodoById(int id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id " + id));
        TodoDTO todoDTO = modelMapper.map(todo, TodoDTO.class);

        return todoDTO;
    }

    @Override
    public void updateTodo(int id, TodoDTO todoDTO) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        Todo todo = optionalTodo.orElseThrow();

        todo.changeContent(todoDTO.getContent());
        todoRepository.save(todo);
    }

    @Override
    public void deleteTodo(int id) {
        todoRepository.deleteById(id);
    }
}
