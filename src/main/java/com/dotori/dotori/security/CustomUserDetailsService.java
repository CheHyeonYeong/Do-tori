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

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername : " + username);

        // DB에 등록된 사용자 정보를 불러옴
        Optional<Auth> result = authRepository.findById(username);

        // 결과가 없는 경우 예외 처리 클래스 호출
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("username not found....");
        }

        Auth auth = result.get();
        AuthSecurityDTO authSecurityDTO = new AuthSecurityDTO(
                auth.getAid(),
                auth.getId(),
                auth.getPassword(),
                auth.getNickName(),
                auth.getEmail(),
                false,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))        //권한 설정
        );

        log.info("userSecurityDTO : {}", authSecurityDTO);

        return authSecurityDTO;

    }
}
