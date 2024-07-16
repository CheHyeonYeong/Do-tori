package com.dotori.dotori.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {

    private int size; // 불러올 게시물 수
    private int total; // 전체 게시물 수
    private int commentCount; //댓글 수
    private boolean realEnd;

    private long currentTime;

    private List<E> postLists;  // 게시글 내용!

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> postLists, int total, boolean realEnd) {
        if(total <= 0) {
            return;
        }
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.realEnd = realEnd;
        this.postLists = postLists;

    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }
}