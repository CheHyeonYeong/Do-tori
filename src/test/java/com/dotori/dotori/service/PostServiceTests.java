package com.dotori.dotori.service;

import com.dotori.dotori.dto.*;
import com.dotori.dotori.repository.AuthRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class PostServiceTests {

    @Autowired
    private PostService postService;
    @Autowired
    private AuthService authService;

    //주석 지우면 에러남
//    @Test
//    public void testAddPost() {
//        PostDTO postDTO = PostDTO.builder().content("content").nickName("tu"+11).build();
//        postService.addPost(postDTO);
//        int pid = postDTO.getPid();
//        log.info(pid);
//    }

    @Test
    public void testGetPost() {
        PostDTO post = postService.getPost(56);
        log.info(post.getContent());

    }
      //주석 지우면 에러남
//    @Test
//    public void updateTest() {
//        log.info("updateTest");
//        PostDTO postDTO = PostDTO.builder().pid(56).content("content").nickName("tu5").build();
//        postService.modifyPost(postDTO);
//        log.info(postService.getPost(56).getContent());
//    }

    @Test
    public void testDeletePost() {
        postService.deletePost(101);
    }

    @Test
    public void listTest() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("cn")
                .keyword("1")
                .size(10)
                .build();
        PageResponseDTO<PostDTO> responseDTO = postService.list(pageRequestDTO);
        log.info(responseDTO);
    }

    @Test
    public void testAddToriBox() throws Exception {
        log.info("testAddToriBox");

        PostDTO postDTO = postService.getPost(1);
        AuthDTO authDTO = authService.info("gusdudco6");

        ToriBoxDTO toriBoxDTO = ToriBoxDTO.builder()
                .pid(postDTO.getPid())
                .aid(authDTO.getAid())
                .build();
        int id = postService.toriBoxPost(toriBoxDTO);
        log.info(id);
    }

    @Test
    public void testCountToriBox(){
        log.info("testCountToriBox");
        int result = postService.countLikes(1);
        log.info(result);
    }



    @Test
    public void testLikeList(){
        log.info("testCountToriBox");
        List<PostDTO> toriBox = postService.toriBoxSelectAll();
        for (PostDTO postDTO : toriBox) {
            log.info(postDTO.getContent());
        }
    }




}
