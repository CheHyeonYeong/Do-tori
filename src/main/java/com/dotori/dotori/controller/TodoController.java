package com.dotori.dotori.controller;

import com.dotori.dotori.dto.TodoDTO;
import com.dotori.dotori.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @PostMapping("/register")
    public String register(@Valid TodoDTO todo, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.info("error occured");
            redirectAttributes.addFlashAttribute("errors", bindingResult);
            return "redirect:/todo/list";
        }
        log.info("registered todo");
        todoService.addTodo(todo);
        return "redirect:/todo/list";
    }

    @PostMapping("/delete")
    public String delete(@Valid TodoDTO todoDTO, RedirectAttributes redirectAttributes) {
        log.info("Delete todo: " + todoDTO);
        todoService.deleteTodo(todoDTO.getId());
        return "redirect:/todo/list";

    }

    @PostMapping("/modify")
    public String modify(@Valid TodoDTO todoDTO, RedirectAttributes redirectAttributes) {
        log.info("Modify todo: " + todoDTO);
        todoService.updateTodo(todoDTO);
        return "redirect:/todo/list";
    }

}