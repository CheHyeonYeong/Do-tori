package com.dotori.dotori.service;

import com.dotori.dotori.dto.UserDTO;

public interface UserService {

    static class MidExistException extends Exception {

        public MidExistException() {}
        public MidExistException(String msg) {
            super(msg);
        }
    }

    void join(UserDTO userDTO) throws MidExistException;

}
