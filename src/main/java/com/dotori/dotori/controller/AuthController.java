package com.dotori.dotori.controller;

import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Log4j2
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 로그인

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDTO authDTO, HttpServletRequest request) {
        authService.login(authDTO);
        HttpSession session = request.getSession(true);
        return ResponseEntity.ok(session.getId());
    }

    // 회원 가입

    @PostMapping("/join")
    public ResponseEntity<AuthDTO> join(@RequestBody AuthDTO authDTO) {
        try {
            Auth auth = Auth.builder()
                    .id(authDTO.getId())
                    .password(authDTO.getPassword())
                    .nickName(authDTO.getNickName())
                    .email(authDTO.getEmail())
                    .social(false)
                    .build();
            Auth joinAuth = authService.join(authDTO);
            AuthDTO responseAuthDTO = AuthDTO.builder()
                    .id(joinAuth.getId())
                    .password(joinAuth.getPassword())
                    .nickName(joinAuth.getNickName())
                    .email(joinAuth.getEmail())
                    .social(false)
                    .build();

            return ResponseEntity.ok().body(responseAuthDTO);
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.badRequest().build();
        }
//        String id = authService.join(authDTO);
//        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(id)
//                .toUri();
//        return ResponseEntity.created(location).body(id);
    }

    // 회원 정보 조회

    @GetMapping("/{id}")
    public ResponseEntity<AuthDTO> getInfo(@PathVariable String id) {
        AuthDTO authDTO = authService.info(id);
        return ResponseEntity.ok(authDTO);
    }

    // 회원 정보 수정

    @PutMapping("/{id}")
    public ResponseEntity<Void> modifyInfo(@PathVariable String id, @RequestBody AuthDTO authDTO) {
        authService.modify(authDTO);
        return ResponseEntity.ok().build();
    }

    // 회원 탈퇴

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInfo(@PathVariable String id) {
        authService.delete(id);
        return ResponseEntity.ok().build();
    }

}
