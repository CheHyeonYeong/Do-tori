package com.dotori.dotori.controller;

import com.dotori.dotori.dto.TodoDTO;
import com.dotori.dotori.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    // 전체 toodo를 보여주는 controller
    @GetMapping("/list")
    public void index(Model model) {
        List<TodoDTO> todos = todoService.getAllTodo();
        model.addAttribute("todos", todos);
    }


}