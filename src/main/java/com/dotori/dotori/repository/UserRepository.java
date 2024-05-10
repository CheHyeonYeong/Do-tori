package com.dotori.dotori.repository;

import com.dotori.dotori.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "roleSet")   // fetch 조인을 위해서 사용한 어노테이션...
    @Query("select u from User u where u.uid = :uid and u.social = false")
    Optional<User> getWithRoles(String uid);

    @EntityGraph(attributePaths = "roleSet")
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "roleSet")
    Optional<User> findByUid(String uid);

    @Modifying
    @Transactional
    @Query("update User u set u.password = :password, u.nickName = :nickName, u.email = :email where u.uid = :uid")
    void updateUser(@Param("password") String password, @Param("nickName") String nickName, @Param("email") String email, @Param("uid") String uid);

}
