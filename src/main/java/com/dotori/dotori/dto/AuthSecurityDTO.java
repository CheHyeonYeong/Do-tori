package com.dotori.dotori.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class AuthSecurityDTO extends User {

    private int aid;
    private String id;
    private String password;
    private String nickName;
    private String email;
    private boolean social;

    private Map<String, Object> props;

    public AuthSecurityDTO(int aid, String id, String password, String nickName, String email, boolean social, Collection<GrantedAuthority> authorities) {
        super(id, password, authorities);
        this.aid = aid;
        this.id = id;
        this.password = password;
        this.nickName = nickName;
        this.email = email;
        this.social = social;
    }

}
