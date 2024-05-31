package com.dotori.dotori.service;

import com.dotori.dotori.dto.CommentDTO;
import com.dotori.dotori.dto.PageRequestDTO;
import com.dotori.dotori.dto.PageResponseDTO;

public interface CommentService {

    int register(CommentDTO commentDTO) throws Exception;

    CommentDTO read(int id);
    void remove(int id);

    PageResponseDTO<CommentDTO> getListOfPost(int pid, PageRequestDTO pageRequestDTO);


}
