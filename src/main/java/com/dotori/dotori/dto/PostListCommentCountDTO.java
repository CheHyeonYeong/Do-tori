package com.dotori.dotori.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class PostListCommentCountDTO {
    private int pid;
    private int aid;
    private String content;
    private String nickName;
    private String profileImage;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int toriBoxCount;
    private Long commentCount;
    private String thumbnail;

    public PostListCommentCountDTO(int pid,int aid, String content, String nickName, String profileImage, LocalDateTime regDate, LocalDateTime modDate, String thumbnail, Long commentCount) {
        this.pid = pid;
        this.aid = aid;
        this.content = content;
        this.profileImage = profileImage;
        this.nickName = nickName;
        this.regDate = regDate;
        this.modDate = modDate;
        this.thumbnail = thumbnail;
        this.commentCount = commentCount;
    }
    // 기본 생성자 추가
    public PostListCommentCountDTO() {
    }
}