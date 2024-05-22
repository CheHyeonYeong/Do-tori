package com.dotori.dotori.security.handler;

import com.dotori.dotori.dto.AuthSecurityDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("onAuthenticationSuccess");
        log.info(authentication.getPrincipal()); // 사용자 정보

        AuthSecurityDTO authSecurityDTO = (AuthSecurityDTO) authentication.getPrincipal();
        String encodedPw = authSecurityDTO.getPassword();

        // 소셜 로그인이고 회원 패스워드가 초기값(1111)인 경우 패스워드 변경 페이지로 리다이렉트
        if (authSecurityDTO.isSocial() && (authSecurityDTO.getPassword().equals("1111")
                || passwordEncoder.matches("1111", authSecurityDTO.getPassword()))) {
            log.info("Should change password");
            log.info("Redirect to Member Modify");
            response.sendRedirect("/auth/modify");
        } else {
            // 패스워드가 초기값이 아닌 경우 todo 리스트 페이지로 리다이렉트
            response.sendRedirect("/todo/list");
        }
    }
}