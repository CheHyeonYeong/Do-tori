package com.dotori.dotori.service;


import com.dotori.dotori.dto.TodoDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TodoService {

    public int createTodo(TodoDTO todoDTO);
//    public List<TodoDTO> getAllTodo(LocalDateTime date);
    public TodoDTO getTodoById(int id);
    public void updateTodo(int id, TodoDTO todoDTO);
    public void deleteTodo(int id);
}
