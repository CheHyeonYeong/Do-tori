package com.dotori.dotori.service.search;

import com.dotori.dotori.dto.PostListReplyCountDTO;
import com.dotori.dotori.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostSearch {

    Page<Post> searchOne(Pageable pageable);
    Page<Post> searchAll(String[] types,String keyword, Pageable pageable);
    Page<PostListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable);       //join 처리함

}

