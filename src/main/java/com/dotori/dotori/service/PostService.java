package com.dotori.dotori.service;

import com.dotori.dotori.dto.PageRequestDTO;
import com.dotori.dotori.dto.PageResponseDTO;
import com.dotori.dotori.dto.PostDTO;
import com.dotori.dotori.entity.Post;

import java.util.List;

public interface PostService {

    // 전체 post 보여주기
    PageResponseDTO<PostDTO> list (PageRequestDTO pageRequestDTO);

    int addPost(PostDTO postDTO);
    PostDTO getPost(int id);
    void modifyPost(PostDTO postDTO);
    void deletePost(int id);
}
