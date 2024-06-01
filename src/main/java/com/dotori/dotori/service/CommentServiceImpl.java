package com.dotori.dotori.service;

import com.dotori.dotori.dto.CommentDTO;
import com.dotori.dotori.dto.PageRequestDTO;
import com.dotori.dotori.dto.PageResponseDTO;
import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.entity.Comment;
import com.dotori.dotori.repository.AuthRepository;
import com.dotori.dotori.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final AuthRepository authRepository;


    @Override
    public int register(CommentDTO commentDTO) throws Exception {

        // Auth 엔티티 가져오기
        Auth auth = authRepository.findById(commentDTO.getAid())
                .orElseThrow(() -> new Exception("Not Found auth id :" + commentDTO.getAid()));

        // 프로필 사진 불러오기
        String profileImage = auth.getProfileImage();
        commentDTO.setProfileImage(profileImage);

        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setPost(commentDTO.getPid());
        comment.setAuth(auth);

        int id = commentRepository.save(comment).getId();

        return id;
    }

    @Override
    public CommentDTO read(int id) {
        Comment comment = commentRepository.findById(id).orElseThrow();
        Auth auth = comment.getAuth();

        // 프로필 이미지 가져오기
        String profileImage = auth.getProfileImage();

        return CommentDTO.builder()
                .id(comment.getId())
                .pid(comment.getPid())
                .content(comment.getContent())
                .aid(auth.getAid())
                .nickName(auth.getNickName())
                .profileImage(profileImage)
                .build();
    }

    @Override
    public void remove(int id) {
        commentRepository.deleteById(id);
    }

    @Override
    public PageResponseDTO<CommentDTO> getListOfPost(int pid, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(0, pageRequestDTO.getSize(), Sort.by("id").ascending());
        Page<Comment> result = commentRepository.listOfPost(pid, pageable);

        List<CommentDTO> dtoList = result.getContent().stream()
                .map(comment -> {
                    Auth auth = comment.getAuth();
                    return CommentDTO.builder()
                            .id(comment.getId())
                            .pid(comment.getPid())
                            .content(comment.getContent())
                            .aid(auth.getAid())
                            .nickName(auth.getNickName())
                            .profileImage(auth.getProfileImage())
                            .build();
                })
                .collect(Collectors.toList());

        return PageResponseDTO.<CommentDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .postLists(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }
}
