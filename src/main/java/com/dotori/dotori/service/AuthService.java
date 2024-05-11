package com.dotori.dotori.service;


import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.repository.AuthRepository;

public interface AuthService {

    static class IdExistException extends Exception {

//        public UidExistException() {}
//        public UidExistException(String msg) {
//            super(msg);
//        }
    }

    void join(AuthDTO authDTO) throws IdExistException;

    boolean checkPassword(String id, String enteredPassword);
}
