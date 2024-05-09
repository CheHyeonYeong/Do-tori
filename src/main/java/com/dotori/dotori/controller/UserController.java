package com.dotori.dotori.controller;

import com.dotori.dotori.dto.UpdateUserDTO;
import com.dotori.dotori.dto.UserJoinDTO;
import com.dotori.dotori.entity.User;
import com.dotori.dotori.repository.UserRepository;
import com.dotori.dotori.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import java.security.Principal;


@Controller
@Log4j2
@RequestMapping("/user")
//@RequestMapping("auth")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserRepository userRepository;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

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
    public String joinPost(UserJoinDTO userJoinDTO, RedirectAttributes redirectAttributes) {
        log.info("join post.....");
        log.info(userJoinDTO);

        try {
            userService.join(userJoinDTO);
        } catch (UserService.UidExistException e) {
            redirectAttributes.addFlashAttribute("error", "mid");
            return "redirect:/user/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/user/login";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/info")
    public String userInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("user not found"));
        model.addAttribute("user", user);
        return "user/userInfo"; // 회원 정보를 보여주는 뷰 이름
    }

    @GetMapping("/modify")
    public String updateGET(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("user not found"));
        model.addAttribute("user", user);
        return "user/modify";
    }

    @PostMapping("/modify")
    public String updatePOST(@ModelAttribute UpdateUserDTO updateUserDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String uid = authentication.getName();
        userRepository.updateUser(updateUserDTO.getPassword(), updateUserDTO.getNickName(), updateUserDTO.getEmail(), uid);
        return "redirect:/user/info";
    }


}
