package com.dotori.dotori.service;

import com.dotori.dotori.dto.PageRequestDTO;
import com.dotori.dotori.dto.PageResponseDTO;
import com.dotori.dotori.dto.PostDTO;
import com.dotori.dotori.entity.Post;
import com.dotori.dotori.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public PageResponseDTO<PostDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageabble("pid"); // 게시글 번호를 기준으로 정렬 -> 다른 방식은 논의 합시다

        Page<Post> result = postRepository.searchAll(types, keyword, pageable);

        List<PostDTO> postDTOS = result.stream()
                .map(posts ->modelMapper.map(posts, PostDTO.class))
                    .collect(Collectors.toList());

        return PageResponseDTO.<PostDTO>withAll()       // postDTO list로 전달하고자 형을 명시한다.
                .pageRequestDTO(pageRequestDTO)
                .postLists(postDTOS)
                .total((int)result.getTotalElements())
                .build();

    }

    @Override
    public int addPost(PostDTO postDTO) {
        Post post = modelMapper.map(postDTO, Post.class);
        int pid = postRepository.save(post).getPid();
        return pid;
    }

    @Override
    public PostDTO getPost(int id) {
        Optional<Post> post = postRepository.findById(id);
        Post result = post.orElseThrow();
        PostDTO postDTO = modelMapper.map(result, PostDTO.class);
        return postDTO;

    }

    @Override
    public void modifyPost(PostDTO postDTO) {
        Optional<Post> post = postRepository.findById(postDTO.getPid());
        Post result = post.orElseThrow();
        result.changePost(postDTO.getContent(), postDTO.getLikeCount());
    }

    @Override
    public void deletePost(int id) {
        postRepository.deleteById(id);
    }
}
