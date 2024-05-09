package com.dotori.dotori.service;


import com.dotori.dotori.dto.UserJoinDTO;

public interface UserService {

    static class UidExistException extends Exception {

//        public UidExistException() {}
//        public UidExistException(String msg) {
//            super(msg);
//        }
    }

    void join(UserJoinDTO userJoinDTO) throws UidExistException;

}
