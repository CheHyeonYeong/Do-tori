package com.dotori.dotori.service;


import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.entity.Auth;

public interface AuthService {

    void login(AuthDTO authDTO);

    Auth join(AuthDTO authDTO);

    AuthDTO info(String id);

    void modify(AuthDTO authDTO);

    void delete(String id);


}
