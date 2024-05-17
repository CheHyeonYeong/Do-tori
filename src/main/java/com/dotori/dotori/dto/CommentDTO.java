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
    private int pid;    // post와 join

    @NotEmpty
    private String content;

    @NotEmpty
    private String nickName;       // auth

}
