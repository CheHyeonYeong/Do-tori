package com.dotori.dotori.dto;

import com.dotori.dotori.entity.Auth;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthDTO {
    private int aid;

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    private String id;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickName;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    private boolean social;

    private String provider;

    // DTO 파일을 통하여 Entity를 생성하는 메소드
    public Auth toEntity() {
        return Auth.builder()
                .nickName(this.nickName)
                .email(this.email)
                .provider(this.provider)
                .build();
    }
}
