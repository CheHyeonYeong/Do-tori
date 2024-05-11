package com.dotori.dotori.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
@ToString
public class AuthSecurityDTO extends User {

    private String id;
    private String password;
    private String nickName;
    private String email;
    private boolean social;

    private Map<String, Object> props;

    public AuthSecurityDTO(String username, String password, String nickName, String email,
                           boolean social) {
        super(username, password, new ArrayList<>());

        this.id = username;
        this.password = password;
        this.nickName = nickName;
        this.email = email;
        this.social = social;
    }





}
