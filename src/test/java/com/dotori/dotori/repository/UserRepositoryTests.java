package com.dotori.dotori.repository;

import com.dotori.dotori.entity.User;
import com.dotori.dotori.entity.UserRole;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@Log4j2
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertUsers() {
        User user = User.builder()
                .uid("test1")
                .password(passwordEncoder.encode("1111"))
                .nickName("tu")
                .email("test@naver.com")
                .build();
        user.addRole(UserRole.USER);

        userRepository.save(user);
    }
}
