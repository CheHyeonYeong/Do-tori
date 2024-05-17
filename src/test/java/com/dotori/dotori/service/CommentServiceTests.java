package com.dotori.dotori.service;

import com.dotori.dotori.dto.CommentDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

@SpringBootTest
@Log4j2
public class CommentServiceTests {
    @Autowired
    private CommentService commentService;

    @Test
    public void testAddComment() {
        log.info("testAddComment");
        log.info(commentService.getClass().getName());

        CommentDTO commentDTO = CommentDTO.builder()
                .pid(100)
                .content("comment test")
                .nickName("tu3")
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
    public void deleteTest() {
        log.info("deleteTest");
        int id = 1;
        commentService.remove(id);       //bno를 이미 지웠기 때문에 재차 remove 시 에러가 뜬다
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            commentService.read(id);
        });
    }



}
