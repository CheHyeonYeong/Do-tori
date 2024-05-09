package com.dotori.dotori.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class UserSecurityDTO extends User {

    private String uid;
    private String password;
    private String nickName;
    private String email;
    private boolean del;
    private boolean social;

    private Map<String, Object> props;

    public UserSecurityDTO(String username, String password, String nickName, String email,
                           boolean del, boolean social, Collection <? extends GrantedAuthority> authorities) {
        super(username, password, authorities);

        this.uid = username;
        this.password = password;
        this.nickName = nickName;
        this.email = email;
        this.del = del;
        this.social = social;
    }





}
