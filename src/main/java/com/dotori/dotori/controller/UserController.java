package com.dotori.dotori.controller;

import com.dotori.dotori.dto.UserDTO;
import com.dotori.dotori.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Log4j2
@RequestMapping("/user")
//@RequestMapping("auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 로그인
    @GetMapping("/login")
    public void loginGET(String error, String logout) {
        log.info("login get......");
        log.info("logout : " + logout);

        if(logout != null){
            log.info("user logout........ ");
        }

    }

    // 회원 가입

    @GetMapping("/join")
    public void joinGET() {
        log.info("join get....");
    }

    @PostMapping("/join")
    public String joinPost(UserDTO userDTO, RedirectAttributes redirectAttributes) {
        log.info("join post");
        log.info(userDTO);

        try {
            userService.join(userDTO);
        } catch (UserService.MidExistException e) {
            redirectAttributes.addFlashAttribute("error", "mid");
            return "fail...";
        }

        redirectAttributes.addFlashAttribute("result", "success");
        return "success";
    }

}
