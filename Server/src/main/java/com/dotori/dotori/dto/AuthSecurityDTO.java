package com.dotori.dotori.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@ToString
public class AuthSecurityDTO extends User implements OAuth2User {

    private int aid;
    private String id;
    private String nickName;
    private String email;
    private boolean social;
    private String profileImage;
    private boolean tutorialDone;


    private Map<String, Object> props;
    public AuthSecurityDTO(int aid, String id, String password, String nickName, String email, boolean social, String profileImage, boolean tutorialDone, Collection<? extends GrantedAuthority> authorities) {
        super(id == null ? "" : id, password == null ? "" : password, authorities);
        this.aid = aid;
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.social = social;
        this.profileImage = profileImage;
        this.tutorialDone = tutorialDone;
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

    @Override
    public Map<String, Object> getAttributes() {
        return props;
    }

    @Override
    public String getName() {
        return id;
    }

}