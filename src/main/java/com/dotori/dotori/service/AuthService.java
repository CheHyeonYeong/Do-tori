package com.dotori.dotori.service;


import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.entity.Auth;

public interface AuthService {

    String login(String id, String password);

    Auth join(AuthDTO authDTO);

    AuthDTO info(String id);
    void modify(AuthDTO authDTO);
    void remove(String id);

}
