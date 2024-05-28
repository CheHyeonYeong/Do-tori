package com.dotori.dotori.service;

import com.dotori.dotori.dto.CommentDTO;
import com.dotori.dotori.dto.PageRequestDTO;
import com.dotori.dotori.dto.PageResponseDTO;
import com.dotori.dotori.entity.Comment;
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

    @Override
    public int register(CommentDTO commentDTO) {
        Comment comment = modelMapper.map(commentDTO, Comment.class);
        comment.setPost(commentDTO.getPid());
        int id = commentRepository.save(comment).getId();
        return id;
    }

    @Override
    public CommentDTO read(int id) {
        Optional<Comment> comment = commentRepository.findById(id);
        Comment result = comment.orElseThrow();
        log.info(result.getId());
        CommentDTO commentDTO = modelMapper.map(result, CommentDTO.class);
        commentDTO.setPid(result.getPid());
        return commentDTO;
    }

    @Override
    public void remove(int id) {
        commentRepository.deleteById(id);
    }

    @Override
    public PageResponseDTO<CommentDTO> getListOfPost(int pid, PageRequestDTO pageRequestDTO) {

        Pageable pageable = (Pageable) PageRequest.of(
                0,
                pageRequestDTO.getSize(),
                Sort.by("id").ascending());

        Page<Comment> result = commentRepository.listOfPost(pid, pageable);

        List<CommentDTO> dtoList = result.getContent().stream().map(comment -> {
            CommentDTO commentDTO = modelMapper.map(comment,CommentDTO.class);
            commentDTO.setPid(comment.getPost().getPid());
            return commentDTO;
        }).collect(Collectors.toList());

        return PageResponseDTO.<CommentDTO>withAll().pageRequestDTO(pageRequestDTO).postLists(dtoList)
                .total((int)result.getTotalElements()).build();

    }
}
