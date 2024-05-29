package com.dotori.dotori.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
public class PostListCommentCountDTO {
    private int pid;
    private String content;
    private String nickName;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int toriBoxCount;
    private Long commentCount;
    private String thumbnail;

    public PostListCommentCountDTO(int pid, String content, String nickName, LocalDateTime regDate, String thumbnail, Long commentCount) {
        this.pid = pid;
        this.content = content;
        this.nickName = nickName;
        this.regDate = regDate;
        this.thumbnail = thumbnail;
        this.commentCount = commentCount;
    }

    // 기본 생성자 추가
    public PostListCommentCountDTO() {
    }
}