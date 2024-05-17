package com.dotori.dotori.security.exception;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class AccessTokenException extends RuntimeException {

    Token_ERROR token_Error;

    // 토큰 오류 유형을 나타내는 열거형
    public enum Token_ERROR {

        // 토큰이 null이거나 너무 짧은 경우를 나타냅니다.
        UNACCEPT(401, "Token is null or too short"),

        // 토큰 타입이 Bearer가 아닌 경우를 나타냅니다.
        BADTYPE(401, "Token type Bearer"),

        // 토큰 형식이 올바르지 않은 경우를 나타냅니다.
        MALFORM(403, "Malformed Token"),

        // 토큰 서명이 유효하지 않은 경우를 나타냅니다.
        BADSIGN(403, "BadSignatured Token"),

        // 토큰이 만료된 경우를 나타냅니다.
        EXPIRD(403, "Expired Token");

        private int status;
        private String msg;

        // 열거형 생성자
        Token_ERROR(int status, String msg) {
            this.status = status;
            this.msg = msg;
        }

        // 상태 코드 반환
        public int getStatus() {
            return status;
        }

        // 오류 메시지 반환
        public String getMsg() {
            return msg;
        }
    }

    // AccessTokenException 생성자
    public AccessTokenException(Token_ERROR error) {
        super(error.name());
        this.token_Error = error;
    }

    // 응답에 오류 정보 전송
    public void sendResponseError(HttpServletResponse resp) {
        resp.setStatus(token_Error.getStatus());
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();
        String responseStr = gson.toJson(Map.of("msg", token_Error.getMsg(), "time", new Date()));

        try {
            resp.getWriter().println(responseStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}