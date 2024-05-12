package com.dotori.dotori.controller;

import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.repository.AuthRepository;
import com.dotori.dotori.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;



@Controller
@Log4j2
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private final AuthRepository authRepository;
    private final AuthService authService;

    // 로그인
    @GetMapping("/login")
    public void loginGet(String error, String logout) {
        log.info("logout : " + logout);
        if(logout != null){
            log.info("user logout..........");
        }
    }

    // 회원 가입

    @GetMapping("/join")
    public void authJoinGet() {
        log.info("join get....");
    }

    @PostMapping("/join")
    public String authJoinPost(AuthDTO authDTO, RedirectAttributes redirectAttributes) {
        log.info("join post.....");
        log.info(authDTO);

        try {
            authService.join(authDTO);
        } catch (AuthService.MidExistException e) {
            redirectAttributes.addFlashAttribute("error", "id");
            return "redirect:/auth/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/auth/login";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/api/info")
    public String authInfo(Model model) {
        log.info("info commin");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        Auth auth = authRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("auth not found"));
        model.addAttribute("auth", auth);
        return "auth/info"; // 회원 정보를 보여주는 뷰 이름
    }

//    @PreAuthorize("principal.username == #authDTO.id")        //이런 인증 절차가 아무것도 없어요ㅠㅠ
    @GetMapping("/modify")
    public String updateGET(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        Auth auth = authRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("auth not found"));
        model.addAttribute("auth", auth);
        return "auth/modify";
    }

    @PostMapping("/modify")
    public String updateAuth(@Valid AuthDTO authDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            log.error("has Error");
            redirectAttributes.addFlashAttribute("errors",bindingResult.getAllErrors());
            redirectAttributes.addAttribute("aid",authDTO.getAid());
            return "redirect:/auth/modify";
        }
        authService.modify(authDTO);
        redirectAttributes.addAttribute("aid",authDTO.getAid());
        return "redirect:/auth/info";
    }

    @PostMapping("/delete")
    public String deleteAuth(AuthDTO authDTO, RedirectAttributes redirectAttributes) {
        String aid = authDTO.getId();
        authService.remove(aid);
        return "redirect:/auth/login";
    }
}