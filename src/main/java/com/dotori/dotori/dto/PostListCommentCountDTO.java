package com.dotori.dotori.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostListCommentCountDTO {

    private int pid;
    private String content;
    private String nickName;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int likeCount;
    private Long commentCount;
    private String thumbnail;
}
