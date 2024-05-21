package com.dotori.dotori.service;

import com.dotori.dotori.dto.PageRequestDTO;
import com.dotori.dotori.dto.PageResponseDTO;
import com.dotori.dotori.dto.PostDTO;
import com.dotori.dotori.dto.PostListCommentCountDTO;
import com.dotori.dotori.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    // 파일 업로드


    // 전체 post 보여주기
    PageResponseDTO<PostDTO> list(PageRequestDTO pageRequestDTO);
    int addPost(PostDTO postDTO, MultipartFile file) throws Exception;
    PostDTO getPost(int id);
    void modifyPost(PostDTO postDTO, MultipartFile file) throws Exception;
    void deletePost(int id);
    PageResponseDTO<PostListCommentCountDTO> listWithCommentCount(PageRequestDTO pageRequestDTO);

}
