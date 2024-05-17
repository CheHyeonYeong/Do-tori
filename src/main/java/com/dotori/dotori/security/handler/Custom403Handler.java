package com.dotori.dotori.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Log4j2
public class Custom403Handler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        log.info("-----------ACCESS DENIED------------");

        // 응답 상태 코드를 403 (Forbidden)으로 설정
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // 요청 헤더에서 Content-Type 값 추출
        String contentType = request.getHeader("Content-Type");

        // Content-Type이 application/json으로 시작하는지 확인
        boolean jsonRequest = contentType.startsWith("application/json");

        log.info("isJSON: " + jsonRequest);

        // JSON 요청이 아닌 경우
        if (!jsonRequest) {
            // 로그인 페이지로 리다이렉트하면서 ACCESS_DENIED 오류 전달
            response.sendRedirect("/auth/login?error=ACCESS_DENIED");
        }
    }
}