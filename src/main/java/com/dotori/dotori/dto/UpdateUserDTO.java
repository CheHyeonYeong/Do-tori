package com.dotori.dotori.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {

    private String password;
    private String nickName;
    private String email;

}
