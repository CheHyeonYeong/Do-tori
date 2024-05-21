package com.dotori.dotori.controller;

import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.repository.AuthRepository;
import com.dotori.dotori.service.AuthService;
import com.dotori.dotori.service.OAuth2Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

    //github 로그인
    @GetMapping("/login/oauth2/code/github")
    public String oauth2LoginSuccess(Authentication authentication) {
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//
//        String email = oAuth2User.getAttribute("email");
//        String userName = oAuth2User.getAttribute("login");
//        String nickName = oAuth2User.getAttribute("name");
//
//        // 이메일을 기준으로 사용자 정보 조회
//        AuthDTO authDTO = authService.info(email);
//
//        if (authDTO == null) {
//
//            // 사용자 정보가 없는 경우 새로운 사용자 생성
//            AuthDTO authDTONotNull = AuthDTO.builder()
//                    .id(email)
//                    .password("social")
//                    .nickName(nickName)
//                    .email(email)
//                    .social(authDTO.isSocial())
//                    .provider(authDTO.getProvider())
//                    .build();
//            Auth auth = authService.join(authDTONotNull);
//        }else {
//            // 사용자 정보가 있는 경우 업데이트
//
//            authDTO.updateUser(nickName, email);
//            authService.modify(authDTO);
//        }

        // 로그인 성공 후 메인 페이지로 리다이렉트
        return "redirect:/todo/list";
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
    public String authJoinPost(AuthDTO authDTO, RedirectAttributes redirectAttributes) {
        log.info("join post.....");
        log.info(authDTO);

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
    public String authInfo(Model model) {
        log.info("info commin");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        AuthDTO authDTO = authService.info(id);
        model.addAttribute("auth", authDTO);
        return "auth/info";
    }

    //    @PreAuthorize("principal.username == #authDTO.id")        //이런 인증 절차가 아무것도 없어요ㅠㅠ
    @GetMapping("/modify")
    public String updateGET(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        AuthDTO authDTO = authService.info(id);
        model.addAttribute("auth", authDTO);
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