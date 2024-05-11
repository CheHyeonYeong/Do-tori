package com.dotori.dotori.controller;

import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.repository.AuthRepository;
import com.dotori.dotori.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    private final PasswordEncoder passwordEncoder;

    // 로그인

    @GetMapping("/login")
    public String loginGet(HttpSession session) {
        String id = (String) session.getAttribute("id");
        if (id != null) {
            return "redirect:/";
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginPOST(String id, String password, HttpSession session) {
        String user = authService.login(id, password);
        if(user == null) {
            return "redirect:/login";
        }
        session.setAttribute("id", id);
        return "redirect:/";
    }

    // 회원 가입

    @GetMapping("/join")
    public void joinGET() {
        log.info("join get....");
    }

    @PostMapping("/join")
    public String joinPost(AuthDTO authDTO, RedirectAttributes redirectAttributes) {
        log.info("join post.....");
        log.info(authDTO);

        try {
            authService.join(authDTO);
        } catch (AuthService.IdExistException e) {
            redirectAttributes.addFlashAttribute("error", "id");
            return "redirect:/auth/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/auth/login";
    }

    @GetMapping("/info")
    public String userInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        Auth auth = authRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("auth not found"));
        model.addAttribute("auth", auth);
        return "auth/info"; // 회원 정보를 보여주는 뷰 이름
    }

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
    public String updatePOST(@ModelAttribute AuthDTO updateAuthDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        authRepository.updateAuth(updateAuthDTO.getPassword(), updateAuthDTO.getNickName(), updateAuthDTO.getEmail(), id);
        return "redirect:/auth/info";
    }

    @PostMapping("/delete")
    public String deletePOST(@ModelAttribute AuthDTO deleteAuthDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        authRepository.deleteAuthBy(id);
        return "redirect:/auth/login";
    }
}
