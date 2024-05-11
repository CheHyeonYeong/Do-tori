package com.dotori.dotori.repository;

import com.dotori.dotori.entity.Auth;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Integer> {

    @Query("select a from Auth a where a.id = :id and a.social = false")
    Optional<Auth> getWithRoles(String id);

    Optional<Auth> findByEmail(String email);

    Optional<Auth> findById(String id);

    @Modifying
    @Transactional
    @Query("update Auth a set a.password = :password, a.nickName = :nickName, a.email = :email where a.id = :id")
    void updateAuth(@Param("password") String password, @Param("nickName") String nickName, @Param("email") String email, @Param("id") String id);

    @Modifying
    @Transactional
    @Query("delete from Auth a where a.id = :id")
    void deleteAuthBy(@Param("id") String id);

}
