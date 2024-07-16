package com.dotori.dotori.repository;

import com.dotori.dotori.entity.Auth;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Integer> {

    Optional<Auth> findById(String id);

    Optional<Auth> findByEmail(String email);
    Optional<Auth> findByNickName(String nickName);
    Optional<Auth> findUserByEmailAndProvider(String email, String provider);

    boolean existsById(String id);
    boolean existsByEmail(String email);
    boolean existsByNickName(String nickName);

    @Modifying
    @Transactional
    @Query("update Auth auth set auth.password = :password, auth.nickName = :nickName, auth.email = :email where auth.id = :id")
    void updateAuth(@Param("password") String password, @Param("nickName") String nickName, @Param("email") String email, @Param("id") String id);

    @Modifying
    @Transactional
    void deleteById(@Param("id") String id);


}
