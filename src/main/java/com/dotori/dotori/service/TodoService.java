package com.dotori.dotori.service;

import com.dotori.dotori.dto.TodoDTO;

import java.util.List;

public interface TodoService {

    // 전체 읽기 Todo는 지금으로써는 애매해서 추후 구현으로 미루겠습니다.

    int addTodo(TodoDTO todoDTO);
    List<TodoDTO> getAllTodo();
    TodoDTO readOneTodo(int id);
    void updateTodo(TodoDTO todoDTO);
    void deleteTodo(int id);
    List<TodoDTO> getTodoByAid(int aid);
}
