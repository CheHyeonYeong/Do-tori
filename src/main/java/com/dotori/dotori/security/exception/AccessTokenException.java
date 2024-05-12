package com.dotori.dotori.security.exception;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class AccessTokenException extends RuntimeException{

    Token_ERROR token_Error;
    public enum Token_ERROR {
        //열거형 제작
        UNACCEPT(401, "Token is null or too short"),
        BADTYPE(401,"Token type Bearer"),
        MALFORM(403,"Malformed Token"),
        BADSIGN(403,"BadSignatured Toekn"),
        EXPIRD(403,"Expired Token");

        private int status;
        private String msg;


        Token_ERROR(int status, String msg){
            this.status = status;
            this.msg = msg;
        }
        public int getStatus(){
            return status;
        }
        public String getMsg(){
            return msg;
        }
    }

    public AccessTokenException(Token_ERROR error){
        super(error.name());
        this.token_Error = error;
    }
    public void sendResponseError(HttpServletResponse resp){
        resp.setStatus(token_Error.getStatus());
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Gson gson = new Gson();
        String responseStr = gson.toJson(Map.of("msg", token_Error.getMsg(), "time", new Date()));

        try{
            resp.getWriter().println(responseStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
