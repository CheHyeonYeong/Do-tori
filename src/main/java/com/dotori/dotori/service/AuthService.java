package com.dotori.dotori.service;


import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.entity.Auth;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    static class MidExistException extends Exception {
        // ID 중복 확인

        public MidExistException(){}
        public MidExistException(String message) {
            super(message);
        }
    }

    static class EmailExistException extends Exception {
        // 이메일 중복 확인

        public EmailExistException(){}
        public EmailExistException(String message) {
            super(message);
        }
    }

    static class NickNameLengthException extends Exception {
        // 닉네임 길이 검사

        public NickNameLengthException() {}
        public NickNameLengthException(String message) {
            super(message);
        }
    }

    Auth join(AuthDTO authDTO) throws MidExistException, EmailExistException, NickNameLengthException;

    AuthDTO info(String id);
    void modify(AuthDTO authDTO);
    void remove(String id);
    String updateProfileImage(String authId, MultipartFile file) throws Exception;

    void updateTutorialDone(String id);
}
