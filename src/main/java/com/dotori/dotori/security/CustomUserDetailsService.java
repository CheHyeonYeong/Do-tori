package com.dotori.dotori.security;

import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.security.dto.AuthSecurityDTO;
import com.dotori.dotori.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername : " + username);

        Optional<Auth> result = authRepository.getWithRoles(username);

        if(result.isEmpty()) {
            throw new UsernameNotFoundException("username not found....");
        }

        Auth auth = result.get();

        AuthSecurityDTO authSecurityDTO = new AuthSecurityDTO(
            auth.getId(),
            auth.getPassword(),
            auth.getNickName(),
            auth.getEmail(),
            false
        );

        log.info("userSecurityDTO");
        log.info(authSecurityDTO);

        return authSecurityDTO;
    }
}
