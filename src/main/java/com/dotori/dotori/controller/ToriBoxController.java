package com.dotori.dotori.controller;

import com.dotori.dotori.dto.ToriBoxDTO;
import com.dotori.dotori.service.ToriBoxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class ToriBoxController {

    private final ToriBoxService toriBoxService;

    @PostMapping("/like")
    public ResponseEntity<?> insert(@RequestBody ToriBoxDTO toriBoxDTO) throws Exception {

        toriBoxService.insert(toriBoxDTO);

        return ResponseEntity.ok(200);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody ToriBoxDTO toriBoxDTO) throws Exception {

        toriBoxService.delete(toriBoxDTO);

        return ResponseEntity.ok(200);
    }

}
