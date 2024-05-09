package com.dotori.dotori.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
@Log4j2
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/")
    public String testController(){
        return "Hello World";
    }
}
