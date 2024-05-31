package com.dotori.dotori.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    private int pid;
    private Long commentCount;
    private int toriBoxCount;

    private int aid;
    private String nickName;
    private String profileImage;

    @NotEmpty
    @Size(min = 1, max = 500)
    private String content;

    @Builder.Default
    private LocalDateTime regDate = LocalDateTime.now();
    private LocalDateTime modDate;

    @Builder.Default
    private List<String> thumbnails = new ArrayList<>();

    // liked 필드 추가
    private boolean liked;

}