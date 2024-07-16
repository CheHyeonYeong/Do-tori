package com.dotori.dotori.repository;

import com.dotori.dotori.entity.Post;
import com.dotori.dotori.service.search.PostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Integer>, PostSearch {

    Page<Post> findByContentContainingOrderByPidDesc(String keyword, Pageable pageable);        // Content로 검색하게끔 들어간다!!

    @Query("select b FROM Post b where b.content like concat('%',:keyword,'%')")
    Page<Post> findKeyword(String keyword, Pageable pageable);

    @Query(value = "select now()", nativeQuery = true)
    String getTime();

    void deleteByAuth_Aid(int aid);

}
