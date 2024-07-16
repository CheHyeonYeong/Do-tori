package com.dotori.dotori.repository;

import com.dotori.dotori.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query("select c from Comment c where c.post.pid = :pid")       // post entity에 있는 post의 pid 참조
    Page<Comment> listOfPost(int pid, Pageable pageable);

    void deleteByAuth_Aid(int aid);

}


