package com.dotori.dotori.repository;

import com.dotori.dotori.entity.Post;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class PostRepositoryTests {
    @Autowired
    private PostRepository postRepository;

    @Test
    public void testInsert() {
        IntStream.rangeClosed(1,100).forEach(i -> {
            Post board = Post.builder()
                    .content("content...................."+i)
                    .nickname("tu"+(i%10+11)) //사용자는 0~9번까지
                    .build();
            Post result = postRepository.save(board);  //JPA는 자동으로 만들어주기 때문에 내가 만들지 않은 save 메소드도 나온다.
            log.info(result);
        });
    }

    @Test
    public void testSelct() {
        int pid = 50;

        Optional<Post> result = postRepository.findById(pid);  //optional Type으로 받아서 처리해야 함
        Post post = result.orElseThrow();
        log.info(post.getContent());
    }

    @Test
    public void testUpdate() {
        int pid = 50;

        Optional<Post> result = postRepository.findById(pid);  //optional Type으로 받아서 처리해야 함
        Post post = result.orElseThrow();
        post.changePost("update title",2);  //modDate 시간은 바뀌나 regdate 시간은 바뀌지 않는다.
        postRepository.save(post);
        log.info(post.getContent());
    }

    @Test
    public void testDelete() {
        int pid = 50;
        Optional<Post> result = postRepository.findById(pid);
        Post post = result.orElseThrow();
        postRepository.delete(post);
    }

}
