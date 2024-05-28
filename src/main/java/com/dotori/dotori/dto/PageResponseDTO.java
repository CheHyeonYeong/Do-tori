package com.dotori.dotori.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {

    private int size;
    private int total;

    private List<E> postLists;  // 게시글 내용!

    @Builder(builderMethodName = "withAll")
    public PageResponseDTO(PageRequestDTO pageRequestDTO, List<E> postLists, int total) {
        if(total <= 0) {
            return;
        }
        this.size = pageRequestDTO.getSize();
        this.total = total;

        this.postLists = postLists;

    }

}