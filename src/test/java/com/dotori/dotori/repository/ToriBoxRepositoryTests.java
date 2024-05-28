package com.dotori.dotori.repository;

import com.dotori.dotori.entity.ToriBox;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


@SpringBootTest
@Log4j2
public class ToriBoxRepositoryTests {

    @Autowired
    private ToriBoxRepository toriBoxRepository;


    @Test
    public void testInsert(){

        ToriBox like = ToriBox.builder()
                .pid(1)
                .aid(1).build();

        toriBoxRepository.save(like);

    }

    @Test
    public void testFindByPid(){
        Optional<ToriBox> like = toriBoxRepository.findById(1);
        log.info("like: {}", like);
        log.info("like: {}", like.get().getAid());
    }

    @Test
    public void testDelete(){
        toriBoxRepository.deleteById(1);
    }


}
