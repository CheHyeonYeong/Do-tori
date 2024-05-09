package com.dotori.dotori.repository;

import com.dotori.dotori.entity.User;
import com.dotori.dotori.entity.UserRole;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertUsers() {

        IntStream.range(2, 21).forEach(i -> {
            User user = User.builder()
                    .uid("test" + i)
                    .password(passwordEncoder.encode("1111"))
                    .nickName("tu" + i)
                    .email("test" + i + "@naver.com")
                    .build();
            user.addRole(UserRole.USER);

            userRepository.save(user);
        });

    }

    @Test
    public void testRead() {
        Optional<User> result = userRepository.getWithRoles("test");

        User user = result.orElseThrow();

        log.info(user);
        log.info(user.getRoleSet());

        user.getRoleSet().forEach(userRole -> log.info(userRole.name()));
    }
}
