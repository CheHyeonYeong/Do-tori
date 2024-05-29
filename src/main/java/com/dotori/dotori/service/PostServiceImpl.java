package com.dotori.dotori.service;

import com.dotori.dotori.dto.*;
import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.entity.Post;
import com.dotori.dotori.entity.PostThumbnail;
import com.dotori.dotori.entity.ToriBox;
import com.dotori.dotori.repository.AuthRepository;
import com.dotori.dotori.repository.PostRepository;
import com.dotori.dotori.repository.ToriBoxRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
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
    private final AuthRepository authRepository;
    private final ToriBoxRepository toriBoxRepository;

    @Override
    public PageResponseDTO<PostDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("pid");

        Page<Post> result = postRepository.searchAll(types, keyword, pageable);

        List<PostDTO> postDTOS = result.stream()
                .map(posts -> {
                    PostDTO postDTO = modelMapper.map(posts, PostDTO.class);
                    List<String> thumbnails = posts.getThumbnails().stream()
                            .map(PostThumbnail::getThumbnail)
                            .collect(Collectors.toList());
                    postDTO.setThumbnails(thumbnails); // 썸네일 설정 추가
                    return postDTO;
                })
                .collect(Collectors.toList());

        return PageResponseDTO.<PostDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .postLists(postDTOS)
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public int addPost(PostDTO postDTO, List<MultipartFile> files) throws Exception {
        Post post = modelMapper.map(postDTO, Post.class);
        List<PostThumbnail> thumbnails = uploadImages(files, post);
        post.getThumbnails().addAll(thumbnails);
        int pid = postRepository.save(post).getPid();
        return pid;
    }

    @Override
    public PostDTO getPost(int id) {
        Optional<Post> post = postRepository.findById(id);
        Post result = post.orElseThrow();
        PostDTO postDTO = modelMapper.map(result, PostDTO.class);
        List<String> thumbnails = result.getThumbnails().stream()
                .map(PostThumbnail::getThumbnail)
                .collect(Collectors.toList());
        postDTO.setThumbnails(thumbnails); // 썸네일 설정 추가
        return postDTO;
    }

    @Override
    public void modifyPost(PostDTO postDTO, List<MultipartFile> files, List<String> deletedThumbnails) throws Exception {
        Optional<Post> post = postRepository.findById(postDTO.getPid());
        Post result = post.orElseThrow();

        if (deletedThumbnails != null && !deletedThumbnails.isEmpty()) {
            for (String filename : deletedThumbnails) {
                result.getThumbnails().removeIf(thumbnail -> thumbnail.getThumbnail().equals(filename));
                // 실제 파일 삭제 로직 추가
            }
        }

        if (files != null && !files.isEmpty()) {
            List<PostThumbnail> thumbnails = uploadImages(files, result);
            result.getThumbnails().addAll(thumbnails);
        }

        result.changePost(postDTO.getContent(), result.getThumbnails());
    }
    private List<PostThumbnail> uploadImages(List<MultipartFile> files, Post post) throws Exception {
        List<PostThumbnail> thumbnails = new ArrayList<>();

        if (files != null) {
            for (MultipartFile file : files) {
                String originalName = file.getOriginalFilename();
                if (originalName != null && !originalName.isEmpty()) {
                    String fileName = System.currentTimeMillis() + "_" + originalName;
                    String savePath = System.getProperty("user.dir") + "/src/main/resources/static/images/";
                    if (!new File(savePath).exists()) {
                        new File(savePath).mkdir();
                    }
                    String filePath = savePath + fileName;
                    file.transferTo(new File(filePath));
                    PostThumbnail postThumbnail = new PostThumbnail(fileName);
                    postThumbnail.setPost(post);
                    thumbnails.add(postThumbnail);
                }
            }
        }

        return thumbnails;
    }

    @Override
    public void deletePost(int id) {
        postRepository.deleteById(id);
    }

    @Override
    public PageResponseDTO<PostDTO> listWithCommentCount(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("pid");

        Page<PostListCommentCountDTO> result = postRepository.searchWithCommentCount(types, keyword, pageable);

        List<PostDTO> postDTOS = result.getContent().stream()
                .map(postListCommentCountDTO -> {
                    PostDTO postDTO = modelMapper.map(postListCommentCountDTO, PostDTO.class);
                    postDTO.setCommentCount(postListCommentCountDTO.getCommentCount());

                    // 썸네일 정보 설정
                    List<String> thumbnails = new ArrayList<>();
                    if (postListCommentCountDTO.getThumbnail() != null) {
                        thumbnails.add(postListCommentCountDTO.getThumbnail());
                    }
                    postDTO.setThumbnails(thumbnails);

                    return postDTO;
                })
                .collect(Collectors.toList());

        return PageResponseDTO.<PostDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .postLists(postDTOS)
                .total((int) result.getTotalElements())
                .build();
    }

    @Transactional
    @Override
    public int toriBoxPost(ToriBoxDTO toriBoxDTO) throws Exception {
        Auth auth = authRepository.findById(toriBoxDTO.getAid())
                .orElseThrow(() -> new Exception("Not Found auth id :" + toriBoxDTO.getAid()));

        Post post = postRepository.findById(toriBoxDTO.getPid())
                .orElseThrow(() -> new Exception("Not Found post id :" + toriBoxDTO.getPid()));

        Optional<ToriBox> existingLike = toriBoxRepository.findByAidAndPost(auth.getAid(), post);

        if (existingLike.isPresent()) {
            ToriBox toriBox = existingLike.get();
            toriBoxRepository.delete(toriBox);
            return toriBox.getId();
        } else {
            ToriBox toriBox = ToriBox.builder().post(post).pid(post.getPid()).aid(auth.getAid()).build();
            ToriBox savedToriBox = toriBoxRepository.save(toriBox);
            return savedToriBox.getId();
        }
    }

    @Override
    public int countLikes(int pid) {
        try{
            Post post = postRepository.findById(pid)
                    .orElseThrow(() -> new Exception("Not Found post id :" + pid));
            return toriBoxRepository.countByPost(post);
        } catch (Exception e) {
            throw new RuntimeException("Error inserting ToriBox", e);
        }
    }

    public List<PostDTO> toriBoxSelectAll() {
        List<ToriBox> toriBoxList = toriBoxRepository.findAll();
        List<PostDTO> toriBoxPosts = toriBoxList.stream()
                .map(toriBox -> modelMapper.map(toriBox.getPost(), PostDTO.class))
                .collect(Collectors.toList());
        return toriBoxPosts;
    }

}