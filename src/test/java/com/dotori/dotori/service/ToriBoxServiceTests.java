package com.dotori.dotori.service;


import com.dotori.dotori.dto.ToriBoxDTO;
import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.entity.Post;
import com.dotori.dotori.entity.ToriBox;
import com.dotori.dotori.repository.AuthRepository;
import com.dotori.dotori.repository.PostRepository;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@Log4j2
@SpringBootTest
public class ToriBoxServiceTests {

    @Autowired
    private ToriBoxService toriBoxService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private AuthRepository authRepository;

    @Test
    public void testAddToriBox() {
        log.info("testAddToriBox");
        log.info(toriBoxService.getClass().getName());

        Optional<Post> post = postRepository.findById(2);
        Optional<Auth> auth = authRepository.findById(1);

        ToriBoxDTO toriBoxDTO = ToriBoxDTO.builder()
                .pid(post.get().getPid())
                .aid(auth.get().getAid())
                .build();
        int id = toriBoxService.insert(toriBoxDTO);
        log.info(id);
    }

    @Test
    public void testSeleteAllToriBox() {
        List<ToriBoxDTO> toriBoxDTOList = toriBoxService.selectAll();
        int i =0;
        for (ToriBoxDTO toriBoxDTO : toriBoxDTOList) {
            log.info(i + "번 : " + toriBoxDTO.getAid());
            i++;
        }
    }

    @Test
    public void testDeleteToriBox() {
        toriBoxService.delete(1);
    }
}
