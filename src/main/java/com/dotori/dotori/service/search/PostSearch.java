package com.dotori.dotori.service.search;

import com.dotori.dotori.dto.PostListCommentCountDTO;
import com.dotori.dotori.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostSearch {

    Page<Post> searchOne(Pageable pageable);
    Page<Post> searchAll(String[] types,String keyword, Pageable pageable);
    Page<PostListCommentCountDTO> searchWithCommentCount(String[] types, String keyword, Pageable pageable);       //join 처리함

}

