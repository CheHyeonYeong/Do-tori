package com.dotori.dotori.service;

import com.dotori.dotori.dto.PageRequestDTO;
import com.dotori.dotori.dto.PageResponseDTO;
import com.dotori.dotori.dto.PostDTO;
import com.dotori.dotori.dto.PostListCommentCountDTO;
import com.dotori.dotori.entity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    // 전체 post 보여주기
    PageResponseDTO<PostDTO> list(PageRequestDTO pageRequestDTO);
    int addPost(PostDTO postDTO, List<MultipartFile> files) throws Exception;
    PostDTO getPost(int id);
    void modifyPost(PostDTO postDTO, List<MultipartFile> files, List<String> deletedThumbnails) throws Exception;
    void deletePost(int id);

    PageResponseDTO<PostDTO> listWithCommentCount(PageRequestDTO pageRequestDTO);


}
