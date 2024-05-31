package com.dotori.dotori.controller;

import com.dotori.dotori.dto.AuthSecurityDTO;
import com.dotori.dotori.dto.TodoDTO;
import com.dotori.dotori.service.TodoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/todo")
public class TodoController {

    private final TodoService todoService;

    // 전체 toodo를 보여주는 controller
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/list")
    public void index(@AuthenticationPrincipal AuthSecurityDTO authSecurityDTO, Model model) {
        int aid = authSecurityDTO.getAid();
        List<TodoDTO> todos = todoService.getTodoByAid(aid);

        // 카테고리 순서대로 배열
        List<String> categoryOrder = List.of("No category", "일정", "공부", "습관");

        Map<String, List<TodoDTO>> todoCategory = todos.stream()
                .collect(Collectors.groupingBy(TodoDTO::getCategory));

        Map<String, List<TodoDTO>> sortedTodoCategory = new LinkedHashMap<>();
        for (String category : categoryOrder) {
            sortedTodoCategory.put(category, todoCategory.getOrDefault(category, List.of()));
        }
        model.addAttribute("todoCategory", sortedTodoCategory);
        model.addAttribute("categoryOrder", categoryOrder);
        model.addAttribute("tutorialDone", authSecurityDTO.isTutorialDone());
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
    public String delete(@RequestParam("id") int id, @RequestParam("todoDate") String todoDate, RedirectAttributes redirectAttributes) {
        log.info("Delete todo: " + id);
        todoService.deleteTodo(id);

        redirectAttributes.addAttribute("selectedDate", todoDate);
        return "redirect:/todo/list";

    }

    @PostMapping("/modify")
    public String modify(@Valid TodoDTO todoDTO, RedirectAttributes redirectAttributes) {
        log.info("Modify todo: " + todoDTO);
        todoService.updateTodo(todoDTO);

        redirectAttributes.addAttribute("selectedDate", todoDTO.getTodoDate().toString());
        return "redirect:/todo/list";
    }

}