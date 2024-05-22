package com.dotori.dotori.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
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

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
