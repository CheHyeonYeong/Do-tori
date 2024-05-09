package com.dotori.dotori.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserJoinDTO {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String uid;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickName;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    private boolean del;
    private boolean social;

}
