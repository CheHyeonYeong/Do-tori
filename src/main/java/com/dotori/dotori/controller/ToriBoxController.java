package com.dotori.dotori.controller;

import com.dotori.dotori.dto.ToriBoxDTO;
import com.dotori.dotori.service.ToriBoxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class ToriBoxController {

    private final ToriBoxService toriBoxService;

    @PostMapping("/like")
    public ResponseEntity<?> insert(@RequestBody ToriBoxDTO toriBoxDTO, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Map<String, Integer> map = new HashMap<>();
        int id = toriBoxService.insert(toriBoxDTO);
        map.put("id", id);

        return ResponseEntity.ok(map);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id){

        toriBoxService.delete(id);
        Map<String, Integer> resultMap = new HashMap<>();
        resultMap.put("id",id);
        return ResponseEntity.ok(resultMap);
    }

}
