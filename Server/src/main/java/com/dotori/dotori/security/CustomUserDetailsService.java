package com.dotori.dotori.security;


import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.dto.AuthSecurityDTO;
import com.dotori.dotori.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // AuthRepository 의존성 주입
    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername : " + username);

        // 사용자 정보를 DB에서 조회
        Optional<Auth> result = authRepository.findById(username);

        // 사용자 정보가 없는 경우 예외 처리
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("username not found : " + username);
        }

        Auth auth = result.get();
        AuthSecurityDTO authSecurityDTO;
        authSecurityDTO = new AuthSecurityDTO(

                auth.getAid(),
                auth.getId(),
                auth.getPassword(),
                auth.getNickName(),
                auth.getEmail(),
                false,
                auth.getProfileImage(),
                auth.isTutorialDone(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );

        log.info("userSecurityDTO : {}", authSecurityDTO);

        return authSecurityDTO;
    }
}