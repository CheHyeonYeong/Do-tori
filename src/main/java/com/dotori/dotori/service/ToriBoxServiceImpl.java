package com.dotori.dotori.service;

import com.dotori.dotori.dto.ToriBoxDTO;
import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.entity.Post;
import com.dotori.dotori.entity.ToriBox;
import com.dotori.dotori.repository.AuthRepository;
import com.dotori.dotori.repository.PostRepository;
import com.dotori.dotori.repository.ToriBoxRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToriBoxServiceImpl implements ToriBoxService {

    private final ToriBoxRepository toriBoxRepository;
    private final AuthRepository authRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public void insert(ToriBoxDTO toriBoxDTO) throws Exception {
        Auth auth = authRepository.findById(toriBoxDTO.getAid())
                .orElseThrow(() -> new Exception("Not Found auth id :" + toriBoxDTO.getAid()));

        Post post = postRepository.findById(toriBoxDTO.getPid())
                .orElseThrow(() -> new Exception("Not Found post id :" + toriBoxDTO.getPid()));

        // 이미 좋아요를 했을 경우
        if (toriBoxRepository.findByAuthAndPost(auth, post).isPresent()) {
            throw new Exception("Like already exist");
        }

        ToriBox toriBox = ToriBox.builder()
                .post(post)
                .auth(auth)
                .build();

        toriBoxRepository.save(toriBox);
    }

    @Override
    @Transactional
    public void delete(ToriBoxDTO toriBoxDTO) throws Exception {
        Auth auth = authRepository.findById(toriBoxDTO.getAid())
                .orElseThrow(() -> new Exception("Not Found auth id :" + toriBoxDTO.getAid()));

        Post post = postRepository.findById(toriBoxDTO.getPid())
                .orElseThrow(() -> new Exception("Not Found post id :" + toriBoxDTO.getPid()));

        ToriBox toriBox = toriBoxRepository.findByAuthAndPost(auth, post)
                .orElseThrow(() -> new Exception("Not Found like id"));

        toriBoxRepository.delete(toriBox);
    }
}
