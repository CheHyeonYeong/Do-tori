package com.dotori.dotori.service;

import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.repository.AuthRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class AuthServiceTests {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private AuthService authService;

    @Test
    public void joinTest() {
        AuthDTO authDTO = AuthDTO.builder()
                .id("TTTTTTTTTTTTTTTTTTT")
                .password("1111")
                .nickName("테스트")
                .email("ttttt@test.com")
                .social(false)
                .build();
        log.info("authDTO: ", authDTO);
        log.info(authService.join(authDTO));
    }

    @Test
    public void infoTest() {
        AuthDTO authDTO = authService.info("test2");
        log.info(authDTO);
    }

    @Test
    public void deleteTest() {
        String id = "TTTTTTTTTTTTTTTTTTTTT";
        authService.delete(id);
    }


}
