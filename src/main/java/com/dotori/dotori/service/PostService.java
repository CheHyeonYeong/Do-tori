package com.dotori.dotori.service;

import com.dotori.dotori.dto.*;
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

    int toriBoxPost(ToriBoxDTO toriBoxDTO) throws Exception;
    int countLikes(int pid);
    boolean isLikedByUser(int pid, int aid);

    List<PostDTO> toriBoxSelectAll(int aid);
    PageResponseDTO<PostDTO> listWithCommentCount(PageRequestDTO pageRequestDTO);


}
