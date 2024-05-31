package com.dotori.dotori.repository;

import com.dotori.dotori.entity.Comment;
import com.dotori.dotori.entity.Post;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class CommentRepositoryTests {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void testInsert(){
        Optional<Post> result = postRepository.findById(15);  //optional Type으로 받아서 처리해야 함
        Post post = result.orElseThrow();
        IntStream.rangeClosed(1,100).forEach(i -> {
            Comment comment = Comment.builder().post(post).content("comment test"+i).build();
            commentRepository.save(comment);
        });
    }


    @Transactional      //lazy 하니가 출력 이후에 적용이 되나, transactional은 쿼리가 다 성공해야 성공처리를 하기 때문에 완료 이후에 가져오게 되어 성공함
    @Test
    public void testPostReplies() {
        //실제 게시물 번호
        int pid = 98;
        Pageable pageable = PageRequest.of(0,10, Sort.by("pid").descending());

        Page<Comment> result = commentRepository.listOfPost(pid,pageable);

        result.getContent().forEach(reply -> {log.info(reply.getId());});

    }



}
