package com.dotori.dotori.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@Builder
@DynamicUpdate // Entity update시, 원하는 데이터만 update하기 위함
@AllArgsConstructor
@NoArgsConstructor
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성
    @Column(name = "aid")
    private int aid;

    @Column(unique = true, name = "user_id")
    private String id;

    private String password;

    @Column(name = "nickName", nullable = false)
    private String nickName;

    // 진짜 이름 받아서 추가하기

    @Column(unique = true)
    private String email;

    @ColumnDefault("false")
    private boolean social;

    @Column(name = "provider")
    private String provider; // 사용자가 로그인한 서비스(ex) google, naver..)

    @Column(name = "profile_image")
    private String profileImage;

    @ColumnDefault("false")
    private boolean tutorialDone;

    public void setAuth(String id) {
        this.id = id;
    }

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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void ProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setTutorialDone(boolean tutorialDone) {this.tutorialDone = tutorialDone;}

    public void changeProfileImage(String profileImage) { this.profileImage = profileImage; }
    // 사용자의 이름이나 이메일을 업데이트하는 메소드
    public Auth updateUser(String nickName, String email) {
        this.nickName = nickName;
        this.email = email;

        return this;
    }

}