package com.dotori.dotori.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private int pid;

    @NotEmpty
    private String nickName;

    @Builder.Default
    private int likeCount = 0;

    @NotEmpty
    @Size(min = 1, max = 100)
    private String content;

    @Builder.Default
    private LocalDateTime regDate = LocalDateTime.now();
    private LocalDateTime modDate;
}
