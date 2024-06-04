package com.dotori.dotori.service;

import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final PostRepository postRepository;
    private final TodoRepository todoRepository;
    private final ToriBoxRepository toriBoxRepository;
    private final CommentRepository commentRepository;

    @Override
    public Auth join(AuthDTO authDTO) throws MidExistException, EmailExistException, NickNameLengthException{
        String mid = authDTO.getId();
        String email = authDTO.getEmail();
        String nickName = authDTO.getNickName();

        if (authRepository.existsById(mid)) {
            throw new MidExistException("이미 존재하는 아이디입니다.");
        }
        if (authRepository.existsByEmail(email)) {
            throw new EmailExistException("이미 존재하는 이메일입니다.");
        }
        if (nickName.length() > 5) {
            throw new NickNameLengthException("닉네임은 5글자 이하여야 합니다.");
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
        Optional<Auth> authOptional = authRepository.findById(id);
        Auth auth = authOptional.orElseThrow(() -> new UsernameNotFoundException("User Not Found...."));
        int aid = auth.getAid();

        postRepository.deleteByAuth_Aid(aid);

        commentRepository.deleteByAuth_Aid(aid);

        toriBoxRepository.deleteByAid(aid);

        todoRepository.deleteByAid(aid);

        authRepository.deleteById(id);
    }

    public String updateProfileImage(String authId, MultipartFile file) throws Exception {
        Auth auth = authRepository.findById(authId).orElseThrow(() -> new IllegalArgumentException("Invalid user id"));

        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String savePath = System.getProperty("user.dir") + "/src/main/resources/static/images/";
            if (!new File(savePath).exists()) {
                new File(savePath).mkdir();
            }
            String filePath = savePath + fileName;
            file.transferTo(new File(filePath));
            auth.setProfileImage(fileName);
            authRepository.saveAndFlush(auth);
            return fileName;
        }
        return null;
    }

    @Override
    public void updateTutorialDone(String id) {
        Optional<Auth> authOptional = authRepository.findById(id);
        Auth auth = authOptional.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        auth.setTutorialDone(true);
        authRepository.save(auth);
    }
}
