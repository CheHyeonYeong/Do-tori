package com.dotori.dotori.security.handler;

import com.dotori.dotori.config.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    // 토큰 발행 의존성 주입
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("onAuthenticationSuccess, Congratulations! For Login Success!!!");

        response.setContentType("application/json");
        Map<String, Object> claims = Map.of("id", authentication.getName());

        // Access Token 유효기간 1일
        String accessToken = jwtUtil.generateToken(claims, 1);
        log.info("JWT Token: " + accessToken); // JWT 토큰 출력

        // Refresh Token 유효기간 30일
        String refreshToken = jwtUtil.generateToken(claims, 30);

        Gson gson = new Gson();
        Map<String, String> keyMap = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );

        String jsonStr = gson.toJson(keyMap);

        response.getWriter().println(jsonStr);
    }
}
