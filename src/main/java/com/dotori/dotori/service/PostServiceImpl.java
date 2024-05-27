package com.dotori.dotori.service;

import com.dotori.dotori.dto.PageRequestDTO;
import com.dotori.dotori.dto.PageResponseDTO;
import com.dotori.dotori.dto.PostDTO;
import com.dotori.dotori.dto.PostListCommentCountDTO;
import com.dotori.dotori.entity.Post;
import com.dotori.dotori.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Pageable pageable = pageRequestDTO.getPageabble("pid");

        Page<Post> result = postRepository.searchAll(types, keyword, pageable);

        List<PostDTO> postDTOS = result.stream()
                .map(posts -> modelMapper.map(posts, PostDTO.class))
                .collect(Collectors.toList());

        // 여기에 postDTOS 썸네일 출력 추가하기!

        return PageResponseDTO.<PostDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .postLists(postDTOS)
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public int addPost(PostDTO postDTO, MultipartFile file) throws Exception {
        String thumbnail = uploadImage(file);
        Post post = modelMapper.map(postDTO, Post.class);
        post.changeThumbnail(thumbnail);
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
    public void modifyPost(PostDTO postDTO, MultipartFile file) throws Exception {
        Optional<Post> post = postRepository.findById(postDTO.getPid());
        Post result = post.orElseThrow();
        String thumbnail = uploadImage(file);
        result.changePost(postDTO.getContent(), postDTO.getLikeCount(), thumbnail);
    }


    private String uploadImage(MultipartFile file) throws Exception {
        String originalName = file.getOriginalFilename();
        String fileName = System.currentTimeMillis() + "_" + originalName;
        String savePath = System.getProperty("user.dir") + "/src/main/resources/static/images/";
        if (!new File(savePath).exists()) {
            new File(savePath).mkdir();
        }
        String filePath = savePath + fileName;
        file.transferTo(new File(filePath));
        return fileName;
    }

    @Override
    public void deletePost(int id) {
        postRepository.deleteById(id);
    }

    @Override
    public PageResponseDTO<PostListCommentCountDTO> listWithCommentCount(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageabble("pid");
        Page<PostListCommentCountDTO> result = postRepository.searchWithCommentCount(types, keyword, pageable);

        return PageResponseDTO.<PostListCommentCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .postLists(result.getContent())
                .total((int) result.getTotalElements())
                .build();
    }
}