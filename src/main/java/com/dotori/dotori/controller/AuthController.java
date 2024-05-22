package com.dotori.dotori.controller;

import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.dto.AuthSecurityDTO;
import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.repository.AuthRepository;
import com.dotori.dotori.service.AuthService;
import com.dotori.dotori.service.OAuth2Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import java.util.Map;


@Controller
@Log4j2
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final OAuth2Service oAuth2Service;
    private final AuthService authService;

    // 로그인
    @GetMapping("/login")
    public void loginGet(String error, String logout) {
        log.info("logout : " + logout);
        if(logout != null){
            log.info("user logout..........");
        }
    }


    @RequestMapping("/oauth")
    public class UserController {
        @GetMapping("/loginInfo")
        public String getJson(Authentication authentication) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            Map<String, Object> attributes = oAuth2User.getAttributes();

            return attributes.toString();
        }
    }



    // 회원 가입

    @GetMapping("/join")
    public void authJoinGet() {
        log.info("join get....");
    }

    @PostMapping("/join")
    public String authJoinPost(@Valid  AuthDTO authDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("join post.....");
        log.info(authDTO);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/auth/join";
        }


        try {
            authService.join(authDTO);

        } catch (AuthService.MidExistException me) {
            redirectAttributes.addFlashAttribute("error", "id");
            return "redirect:/auth/join";
        } catch (AuthService.NickNameExistException ne) {
            redirectAttributes.addFlashAttribute("error", "nickName");
            return "redirect:/auth/join";
        } catch (AuthService.EmailExistException ee) {
            redirectAttributes.addFlashAttribute("error", "email");
            return "redirect:/auth/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/auth/login";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/info")
    public String authInfo(@AuthenticationPrincipal AuthSecurityDTO authSecurityDTO, Model model) {
        log.info("info coming");
        model.addAttribute("auth", authSecurityDTO);
        return "auth/info";
    }

    //    @PreAuthorize("principal.username == #authDTO.id")        //이런 인증 절차가 아무것도 없어요ㅠㅠ
    @GetMapping("/modify")
    public String updateGET(@AuthenticationPrincipal AuthSecurityDTO authSecurityDTO, Model model) {
        model.addAttribute("auth", authSecurityDTO);
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