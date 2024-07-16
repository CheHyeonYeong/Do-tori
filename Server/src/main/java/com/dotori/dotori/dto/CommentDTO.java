package com.dotori.dotori.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private int id;

    @NotNull
    private int pid;    // post 참조

    @NotEmpty
    private String content;

    // auth 참조
    private int aid;
    private String nickName;
    private String profileImage;

}
