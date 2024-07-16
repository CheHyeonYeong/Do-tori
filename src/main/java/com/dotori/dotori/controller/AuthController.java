package com.dotori.dotori.controller;

import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.dto.AuthSecurityDTO;
import com.dotori.dotori.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import java.util.Map;

@Controller
@Log4j2
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    // 로그인
    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public void loginGet(String error, String logout) {
        log.info("login get...");
        log.info("logout : " + logout);
        if(logout != null){
            log.info("user logout..........");
        }
    }

    @GetMapping("/")
    public String rememberMeLogin() {
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

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public void authJoinGet() {
        log.info("join get....");
    }

    @PreAuthorize("isAnonymous()")
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
        } catch (AuthService.EmailExistException ee) {
            redirectAttributes.addFlashAttribute("error", "email");
            return "redirect:/auth/join";
        } catch (AuthService.NickNameLengthException le) {
            redirectAttributes.addFlashAttribute("error", "nickNameLength");
            return "redirect:/auth/join";
        }

        redirectAttributes.addFlashAttribute("result", "success");
        return "redirect:/auth/login";
    }



    @PreAuthorize("principal.username == #authSecurityDTO.id")
    @GetMapping("/info")
    public String authInfo(@AuthenticationPrincipal AuthSecurityDTO authSecurityDTO, Model model) {
        log.info("info coming");
        model.addAttribute("auth", authSecurityDTO);

        String id = authSecurityDTO.getId();
        AuthDTO authDTO = authService.info(id);
        model.addAttribute("auth", authDTO);

        // 소셜 로그인 여부 확인
        if (authDTO.isSocial() || authDTO.getProvider() != null) {
            return "auth/socialMyPage"; // 소셜 로그인한 회원의 정보 페이지
        } else {
            return "auth/info"; // 일반 로그인한 회원의 정보 페이지
        }
    }

    @PreAuthorize("principal.username == #authSecurityDTO.id")
    @PostMapping("/info")
    public String PostauthInfo(@AuthenticationPrincipal AuthSecurityDTO authSecurityDTO, RedirectAttributes redirectAttributes, Model model) {
        authService.info(authSecurityDTO.getId());
        model.addAttribute("auth", authSecurityDTO);
        return "redirect:/auth/info";
    }

    @PreAuthorize("principal.username == #authSecurityDTO.id")
    @PostMapping("/profile-image")
    public String updateProfileImage(@AuthenticationPrincipal AuthSecurityDTO authSecurityDTO,
                                     @RequestParam("file") MultipartFile file,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes,
                                     Model model, HttpSession session , HttpServletResponse response
    ) {
        try {
            String authId = authentication.getName();
            String fileName = authService.updateProfileImage(authId, file);

            // Authentication 객체 업데이트
            Authentication updatedAuthentication = SecurityContextHolder.getContext().getAuthentication();
            AuthSecurityDTO updatedAuthSecurityDTO = (AuthSecurityDTO) updatedAuthentication.getPrincipal();
            updatedAuthSecurityDTO.setProfileImage(fileName);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(updatedAuthSecurityDTO, updatedAuthentication.getCredentials(), updatedAuthentication.getAuthorities())
            );

            // 세션 업데이트
            session.setAttribute("profileImage", fileName);

            redirectAttributes.addFlashAttribute("message", "Profile image updated successfully");
            model.addAttribute("auth", updatedAuthSecurityDTO);
        } catch (Exception e) {
            log.error("Failed to update profile image", e);
            redirectAttributes.addFlashAttribute("error", "Failed to update profile image");
        }

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        return "redirect:/auth/info";
    }
    @PreAuthorize("principal.username == #authSecurityDTO.id")
    @GetMapping("/modifyPwCheck")
    public String modifyPwCheck(@AuthenticationPrincipal AuthSecurityDTO authSecurityDTO, Model model) {
        log.info("modifyPwCheck coming...");
        model.addAttribute("auth", authSecurityDTO);
        return "auth/modifyPwCheck";
    }
    @PostMapping("/modifyPwCheck")
    public String pwCheck(@AuthenticationPrincipal AuthSecurityDTO authSecurityDTO, @RequestParam("password") String password, HttpSession session, RedirectAttributes redirectAttributes) {
        AuthDTO authDTO = authService.info(authSecurityDTO.getId());

        if(password != null && passwordEncoder.matches(password, authDTO.getPassword())){
            return "redirect:/auth/modify";
        } else {
            redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "redirect:/auth/modifyPwCheck";
        }


    }

    @PreAuthorize("principal.username == #authSecurityDTO.id")        //이런 인증 절차가 아무것도 없어요ㅠㅠ
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

        // Authentication 객체 업데이트
        // SecurityContextHolder로부터 현재 인증 정보(Authentication)를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 현재 인증 정보에서 Principal을 가져와 AuthSecurityDTO로 캐스팅합니다.
        AuthSecurityDTO updatedAuthSecurityDTO = (AuthSecurityDTO) authentication.getPrincipal();
        // 수정된 정보(authDTO)에서 닉네임을 가져와 AuthSecurityDTO의 닉네임을 업데이트합니다.
        updatedAuthSecurityDTO.setNickName(authDTO.getNickName());

        // 업데이트된 AuthSecurityDTO, 기존 인증 정보의 자격 증명(Credentials)과 권한(Authorities)을 사용하여
        // 새로운 UsernamePasswordAuthenticationToken을 생성합니다.
        // 이렇게 생성된 새로운 인증 정보를 SecurityContextHolder에 설정함으로써 인증 정보를 업데이트합니다.
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(updatedAuthSecurityDTO, authentication.getCredentials(), authentication.getAuthorities())
        );

        redirectAttributes.addAttribute("aid",authDTO.getAid());
        return "redirect:/auth/info";
    }

    @PreAuthorize("principal.username == #authDTO.id")
    @PostMapping("/delete")
    public String deleteAuth(AuthDTO authDTO, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String aid = authDTO.getId();
        authService.remove(aid);

        // 사용자의 인증 정보 제거
        HttpSession session =request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();

        return "redirect:/auth/login";
    }

    @PreAuthorize("principal.username == #authSecurityDTO.id")
    @PostMapping("/tutorialDone")
    public String tutorialDone(@AuthenticationPrincipal AuthSecurityDTO authSecurityDTO, Authentication authentication) {
        authService.updateTutorialDone(authSecurityDTO.getId());

        // Authentication 객체 업데이트
        AuthSecurityDTO updatedAuthSecurityDTO = (AuthSecurityDTO) authentication.getPrincipal();
        updatedAuthSecurityDTO.setTutorialDone(true);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(updatedAuthSecurityDTO, authentication.getCredentials(), authentication.getAuthorities())
        );

        return "redirect:/todo/list";
    }
}