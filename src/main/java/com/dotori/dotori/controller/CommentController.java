package com.dotori.dotori.controller;

import com.dotori.dotori.dto.CommentDTO;
import com.dotori.dotori.dto.PageRequestDTO;
import com.dotori.dotori.dto.PageResponseDTO;
import com.dotori.dotori.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/")
    public Map<String, Integer> registerComment(@RequestBody CommentDTO commentDTO, BindingResult bindingResult) throws Exception{
        log.info("registerComment");
        if (bindingResult.hasErrors()) {
            log.info("register comment failed" + bindingResult.getAllErrors());
            throw new BindException(bindingResult);
        }

        Map<String, Integer> map = new HashMap<>();
        int id = commentService.register(commentDTO);
        map.put("id", id);
        return map;
    }

    @GetMapping(value = "/list/{pid}")
    public PageResponseDTO<CommentDTO> getList(@PathVariable("pid") int pid, PageRequestDTO pageRequestDTO){
        PageResponseDTO<CommentDTO> responseDTO = commentService.getListOfPost(pid, pageRequestDTO);
        responseDTO.setCommentCount(responseDTO.getTotal());
        return responseDTO;
    }

    @GetMapping("/{id}")
    public CommentDTO getComment(@PathVariable("id") int id){
        CommentDTO commentDTO = commentService.read(id);
        return commentDTO;
    }

    @DeleteMapping("/{id}")
    public Map<String, Integer> deleteComment(@PathVariable("id") int id){
        commentService.remove(id);

        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("id",id);
        return resultMap;
    }


}
