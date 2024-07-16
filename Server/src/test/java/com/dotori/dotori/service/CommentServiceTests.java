package com.dotori.dotori.service;

import com.dotori.dotori.config.RootConfig;
import com.dotori.dotori.dto.CommentDTO;
import com.dotori.dotori.dto.PageRequestDTO;
import com.dotori.dotori.dto.PageResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.NoSuchElementException;

@SpringBootTest
@Log4j2
@Import(RootConfig.class)
public class CommentServiceTests {
    @Autowired
    private CommentService commentService;

    @Test
    public void testAddComment() throws Exception {
        log.info("testAddComment");
        log.info(commentService.getClass().getName());

        CommentDTO commentDTO = CommentDTO.builder()
                .pid(2)
                .content("comment test2")
                .aid(1)
                .build();
        int id = commentService.register(commentDTO);
        log.info("id is {}", id);
    }

    @Test
    public void selectTest() {
        log.info("selectTest");
        CommentDTO commentDTO = commentService.read(1);
        log.info(commentDTO.getContent());
    }

    @Test
    public void listTest() {
        log.info("listTest");
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        PageResponseDTO<CommentDTO> commentDTO = commentService.getListOfPost(2, pageRequestDTO);
        log.info(commentDTO);
    }

    @Test
    public void deleteTest() {
        log.info("deleteTest");
        int id = 1;
        commentService.remove(id);       // pid 이미 지웠기 때문에 재차 remove 시 에러가 뜬다
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            commentService.read(id);
        });
    }



}
