package com.dotori.dotori.repository;

import com.dotori.dotori.dto.PostListCommentCountDTO;
import com.dotori.dotori.entity.Post;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class PostRepositoryTests {
    @Autowired
    private PostRepository postRepository;

    @Test
    public void testInsert() {
        IntStream.rangeClosed(1,20).forEach(i -> {
            Post post = Post.builder()
                    .content("content...................."+i)
                    .nickName("tu"+(i%10+11)) //사용자는 0~9번까지
                    .build();
            Post result = postRepository.save(post);  //JPA는 자동으로 만들어주기 때문에 내가 만들지 않은 save 메소드도 나온다.
            log.info(result);
        });
    }

    @Test
    public void testSelect() {
        int pid = 50;

        Optional<Post> result = postRepository.findById(pid);  //optional Type으로 받아서 처리해야 함
        Post post = result.orElseThrow();
        log.info(post.getContent());
    }

//    @Test
//    public void testUpdate() {
//        int pid = 50;
//
//        Optional<Post> result = postRepository.findById(pid);  //optional Type으로 받아서 처리해야 함
//        Post post = result.orElseThrow();
//        post.changePost("update title",2);  //modDate 시간은 바뀌나 regdate 시간은 바뀌지 않는다.
//        postRepository.save(post);
//        log.info(post.getContent());
//    }

    @Test
    public void testDelete() {
        int pid = 50;
        Optional<Post> result = postRepository.findById(pid);
        Post post = result.orElseThrow();
        postRepository.delete(post);
    }

    // ------------------- Paging test -------------------------
    @Test
    public void testFindAll() {

        //1. PAGE order by pid desc

        Pageable pageable = PageRequest.of(0,10, Sort.by("pid").descending());
        Page<Post> result = postRepository.findAll(pageable);
        log.info("total count : " + result.getTotalElements());   // 전체 게시글의 수가 있음
        log.info("total pages : " + result.getTotalPages());      // 전체 페이지 개수
        log.info("pages number : " + result.getNumber());         // 페이지 번호
        log.info("pages size : " + result.getSize());             // 페이지 사이즈
        log.info("pages has previous : " + result.hasPrevious());             // 이전 페이지가 있냐
        log.info("pages has Next : " + result.hasNext());             // 다음 페이지가 있냐

        List<Post> postList = result.getContent();
        postList.forEach(post -> {
            log.info(post.getContent());
        });
    }

    @Test
    public void testQueryAnnotation() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("pid").descending());

        String title = "...";
        Page<Post> result =  postRepository.findByContentContainingOrderByPidDesc(title,pageable);
        result.forEach(post -> log.info(post.getContent()));
    }

    @Test
    public void testGetTime(){
        log.info(postRepository.getTime());
    }

    @Test
    public void testSearch(){
        //2번 페이지에 있는 order By bno desc
        Pageable pageable = PageRequest.of(1,10, Sort.by("pid").descending());

        postRepository.searchOne(pageable);
    }

    @Test
    public void testSearchAll() {
        String[] types = {"c","w"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(1,10,Sort.by("pid").descending());

        Page<Post> result = postRepository.searchAll(types,keyword,pageable);
        result.getContent().forEach(board -> log.info(board));
        log.info("사이즈 : "+ result.getSize());
        log.info("페이지번호 : "+ result.getNumber());
        log.info("이전페이지 : "+ result.hasPrevious());
        log.info("다음페이지 : "+ result.hasNext());
    }

    @Test
    public void testSearchWithCommentCount(){

        String[] types = {"c","n"};
        String keyword = "1";
        Pageable pageable = PageRequest.of(0,10,Sort.by("pid").descending());
        Page<PostListCommentCountDTO> result = postRepository.searchWithCommentCount(types, keyword, pageable);

        result.getContent().forEach(postListCommentCountDTO -> {
            log.info("+" + postListCommentCountDTO);
        });

    }



}
