package com.dotori.dotori.repository;

import com.dotori.dotori.entity.Auth;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class AuthRepositoryTests {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertUsers() {

        IntStream.range(2, 21).forEach(i -> {
            Auth auth = Auth.builder()
                    .id("test" + i)
                    .password(passwordEncoder.encode("1111"))
                    .nickName("tu" + i)
                    .email("test" + i + "@naver.com")
                    .build();

            authRepository.save(auth);
        });
    }


    @Test
    public void testRead(){
        Optional<Auth> auth = authRepository.findById("test5");
        Auth auth1 = auth.orElseThrow();

        log.info(auth1);
        log.info(auth1.getAid());

        log.info(auth1.getNickName());
    }

}
