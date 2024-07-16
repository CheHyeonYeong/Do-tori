package com.dotori.dotori.service;

import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.repository.AuthRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootTest
@Log4j2
public class AuthServiceTests {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    //login은 기본적으로 제공하기 때문에, 따로 테스트를 진행하지 않음

    @Test
    public void joinTest() throws AuthService.MidExistException, AuthService.EmailExistException, AuthService.NickNameLengthException {
        AuthDTO authDTO = AuthDTO.builder()
                .id("ServiceTestUser2")
                .password("1111")
                .nickName("테스트2")
                .email("ServiceTestUser3@test.com")
                .social(true)
                .tutorialDone(true)
                .build();
        log.info("authDTO: ", authDTO.getAid());
        log.info(authService.join(authDTO));
    }

    @Test
    public void infoTest() {
        AuthDTO authDTO = authService.info("test2");
        log.info(authDTO);
    }

    @Test
    public void modifyTest(){
        AuthDTO authDTO = AuthDTO.builder()
                .id("ServiceTestUser2")
                .password("Servicetest1")
                .nickName("테스트50")
                .email("ServiceTestUser30@test.com")
                .social(true)
                .tutorialDone(true)
                .build();
        authService.modify(authDTO);
        Optional<Auth> auth=authRepository.findById("ServiceTestUser2");
        log.info(auth.get().getNickName());
        log.info(auth.get().getEmail());
    }

    @Test
    public void deleteTest() {
        String id = "ServiceTestUser2";
        authService.remove(id);
    }


}
