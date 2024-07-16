package com.dotori.dotori.repository;

import com.dotori.dotori.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findByAid(int aid);

    void deleteByAid(int aid);

}
