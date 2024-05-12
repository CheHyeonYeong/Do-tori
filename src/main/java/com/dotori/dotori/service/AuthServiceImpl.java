package com.dotori.dotori.service;

import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.repository.AuthRepository;
import com.dotori.dotori.security.dto.AuthSecurityDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Override
    public Auth join(AuthDTO authDTO) throws MidExistException{
        String mid = authDTO.getId();

        if (authRepository.existsById(mid)) {
            throw new MidExistException("이미 존재하는 아이디입니다.");
        }

        Auth auth = modelMapper.map(authDTO, Auth.class);
        auth.changePassword(passwordEncoder.encode(authDTO.getPassword()));       //password는 암호화

        log.info("생성된 auth : " + auth);

        return authRepository.save(auth);
    }

    @Override
    public AuthDTO info(String id) {
        Optional<Auth> authOptional = authRepository.findById(id);
        Auth auth = authOptional.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        AuthDTO authDTO = modelMapper.map(auth, AuthDTO.class);
        log.info("info AuthDTO : " + authDTO);
        authDTO.setId(auth.getId());
        return authDTO;
    }

    @Override
    public void modify(AuthDTO authDTO) {
        Optional<Auth> authOptional = authRepository.findById(authDTO.getId());
        Auth auth = authOptional.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        auth.changePassword(passwordEncoder.encode(authDTO.getPassword()));
        auth.changeNickName(authDTO.getNickName());
        auth.changeEmail(authDTO.getEmail());
        authRepository.save(auth);
    }

    @Override
    public void remove(String id) {
        authRepository.deleteById(id);
    }

}
