package com.dotori.dotori.repository;

import com.dotori.dotori.entity.Auth;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

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
                    .provider("naver")
                    .social(true)
                    .tutorialDone(true)
                    .build();

            authRepository.save(auth);
        });
    }


    @Test
    public void testRead(){
        Optional<Auth> auth = authRepository.findById("test10");
        Auth auth1 = auth.orElseThrow();

        log.info(auth1);
        log.info(auth1.getAid());

        log.info(auth1.getNickName());
    }

    @Test
    public void testByNickName(){
        Optional<Auth> auth = authRepository.findByNickName("tu10");
        Auth auth1 = auth.orElseThrow();

        log.info(auth1);
        log.info(auth1.getAid());

        log.info(auth1.getNickName());
    }

    @Test
    public void testByEmail(){
        Optional<Auth> auth = authRepository.findByEmail("test10@naver.com");
        Auth auth1 = auth.orElseThrow();
        log.info(auth1);
        log.info(auth1.getAid());
        log.info(auth1.getNickName());
    }

    @Test
    public void testByProvider(){
        Optional<Auth> auth = authRepository.findUserByEmailAndProvider("test10@naver.com", "naver");
        Auth auth1 = auth.orElseThrow();

        log.info(auth1);
        log.info(auth1.getAid());

        log.info(auth1.getNickName());
    }

    @Commit
    @Test
    public void testUpdate(){
        String id = "test10";
        String updatePassword = passwordEncoder.encode("test10");
        String updateNickName = "test10";
        String updateEmail = "test10@naver.com";
        authRepository.updateAuth(updatePassword, updateNickName,updateEmail, id);

        log.info(authRepository.findById(id).get());
    }

    @Test
    public void testDelete(){
        testRead();
        String id = "test10";
        authRepository.deleteById(id);

    }
}
