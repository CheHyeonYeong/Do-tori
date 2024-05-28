package com.dotori.dotori.service;

import com.dotori.dotori.dto.CommentDTO;
import com.dotori.dotori.dto.PageRequestDTO;
import com.dotori.dotori.dto.PageResponseDTO;
import com.dotori.dotori.dto.ToriBoxDTO;
import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.entity.Comment;
import com.dotori.dotori.entity.Post;
import com.dotori.dotori.entity.ToriBox;
import com.dotori.dotori.repository.AuthRepository;
import com.dotori.dotori.repository.PostRepository;
import com.dotori.dotori.repository.ToriBoxRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ToriBoxServiceImpl implements ToriBoxService {

    private final ToriBoxRepository toriBoxRepository;
    private final AuthRepository authRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public int insert(ToriBoxDTO toriBoxDTO) {
        try {
            Auth auth = authRepository.findById(toriBoxDTO.getAid())
                    .orElseThrow(() -> new Exception("Not Found auth id :" + toriBoxDTO.getAid()));

            Post post = postRepository.findById(toriBoxDTO.getPid())
                    .orElseThrow(() -> new Exception("Not Found post id :" + toriBoxDTO.getPid()));

            // ModelMapper를 사용하여 DTO에서 엔티티로 매핑
            ToriBox toriBox = modelMapper.map(toriBoxDTO, ToriBox.class);
            toriBox.setPost(post.getPid());

            // 만일 이미 있는 경우에는 exception 발생
            if (toriBoxRepository.findByAidAndPost(toriBox.getAid(), post).isPresent()) {
                throw new Exception("Like already exists");
            }

            ToriBox savedToriBox = toriBoxRepository.save(toriBox);
            return savedToriBox.getId();

        } catch (Exception e) {
            throw new RuntimeException("Error inserting ToriBox", e);
        }
    }

    @Override
    public List<ToriBoxDTO> selectAll() {
        List<ToriBox> toriBoxList = toriBoxRepository.findAll();
        List<ToriBoxDTO> toriBoxDTOList = toriBoxList.stream()
                .map(toriBox -> modelMapper.map(toriBox, ToriBoxDTO.class))
                .collect(Collectors.toList());
        return toriBoxDTOList;
    }

    @Override
    @Transactional
    public void delete(int id) {
        toriBoxRepository.deleteById(id);
    }
}
