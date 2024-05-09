package com.dotori.dotori.repository;

import com.dotori.dotori.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    @EntityGraph(attributePaths = "roleSet")   // fetch 조인을 위해서 사용한 어노테이션...
    @Query("select m from User m where m.uid = :uid and m.social = false")
    Optional<User> getWithRoles(String uid);

    @EntityGraph(attributePaths = "roleSet")
    Optional<User> findByEmail(String email);

    @Modifying  // 이 어노테이션을 사용하면 @Query에서 DML(insert/update/delete)처리를 가능하게 함
    @Transactional
    @Query("update User m set m.password = :password where m.uid = :uid ")
    void updatePassword(@Param("password") String password, @Param("uid") String uid);

}
