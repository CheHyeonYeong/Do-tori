package com.dotori.dotori.dto;

import com.dotori.dotori.entity.Auth;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

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
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 최소 8자 이상이어야 하며, 영문 대/소문자, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickName;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    private boolean social;

    private String provider;

    private String profileImage;

    //튜토리얼 추가
    private boolean tutorialDone;

    // DTO 파일을 통하여 Social 로그인 시 Auth Entity를 생성하는 메소드
    public Auth toEntity() {
        return Auth.builder()
                .id(this.email)
                .password("1234")
                .nickName(this.nickName)
                .email(this.email)
                .provider(this.provider)
                .profileImage(null)
                .social(true)
                .tutorialDone(this.tutorialDone)
                .build();
    }

}
