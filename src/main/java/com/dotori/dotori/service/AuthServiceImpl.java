package com.dotori.dotori.service;

import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.repository.AuthRepository;
import com.dotori.dotori.security.CustomUserDetailsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final HttpSession httpSession;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    public void join(AuthDTO authDTO) throws IdExistException {

        String id = authDTO.getId();

        Optional<Auth> exists = authRepository.findById(id);

        // 중복 검사
        if(exists.isPresent() || !exists.isEmpty()) {
            log.info("User already exists");
            throw new IdExistException();
        }
        Auth auth = modelMapper.map(authDTO, Auth.class);
        auth.changePassword(passwordEncoder.encode(authDTO.getPassword()));

        log.info("UserJoinDTO Start");
        log.info(auth);

        authRepository.save(auth);

    }

    @Override
    public String login(String id, String password) {
        Optional<Auth> auth = authRepository.getWithRoles(id);
        if(auth.get().getPassword().equals(password)) {
            return auth.get().getId();
        }
        return null;
//
//        Optional<Auth> authOptional = authRepository.findById(authDTO.getId());
//        if(authOptional.isPresent()) {
//            Auth auth = authOptional.get();
//            if(passwordEncoder.matches(authDTO.getPassword(), auth.getPassword())) {
//                return true;
//            }
//        }
//        return false;
    }
}
