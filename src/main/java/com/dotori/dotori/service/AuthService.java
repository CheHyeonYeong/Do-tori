package com.dotori.dotori.service;


import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.entity.Auth;

public interface AuthService {
    static class MidExistException extends Exception {
        //ID 가 중복되면 안됨!! 예외발생하기 때문에 예외처리를 함

        public MidExistException(){}
        public MidExistException(String message) {
            super(message);
        }
    }

    Auth join(AuthDTO authDTO) throws MidExistException;

    AuthDTO info(String id);
    void modify(AuthDTO authDTO);
    void remove(String id);

}
