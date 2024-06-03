package com.dotori.dotori.repository;

import com.dotori.dotori.entity.Post;
import com.dotori.dotori.entity.ToriBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ToriBoxRepository extends JpaRepository<ToriBox, Integer> {

    Optional<ToriBox> findByAidAndPost(int aid, Post post);
    int countByPost(Post post);

    List<ToriBox> findByAid(int aid);

    void deleteByAid(int aid);

}
