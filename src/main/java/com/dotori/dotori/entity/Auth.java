package com.dotori.dotori.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성
    private int aid;

    @NotNull
    @Column(unique = true)
    private String id;

    @NotNull
    private String password;

    @NotNull
    @Column(unique = true)
    private String nickName;

    @NotNull
    private String email;

    @ColumnDefault("false")
    private boolean social;

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeNickName(String nickName) {
        this.nickName = nickName;
    }

    public void changeEmail(String email) {
        this.email = email;
    }

    public void changeSocial(boolean social) {
        this.social = social;
    }


}
