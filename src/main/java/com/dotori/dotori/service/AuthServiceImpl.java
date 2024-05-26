package com.dotori.dotori.service;

import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.dto.AuthSecurityDTO;
import com.dotori.dotori.dto.PostDTO;
import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.entity.Post;
import com.dotori.dotori.repository.AuthRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public Auth join(AuthDTO authDTO) throws MidExistException, NickNameExistException, EmailExistException{
        String mid = authDTO.getId();
        String email = authDTO.getEmail();
        String nickName = authDTO.getNickName();

        if (authRepository.existsById(mid)) {
            throw new MidExistException("이미 존재하는 아이디입니다.");
        }
        if (authRepository.existsByNickName(nickName)) {
            throw new NickNameExistException("이미 존재하는 닉네임입니다.");
        }
        if (authRepository.existsByEmail(email)) {
            throw new EmailExistException("이미 존재하는 이메일입니다.");
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

//        try {
//            String profile = uploadImage(profileImage);
//            auth.changeProfileImage(uploadImage(profileImage));
//        } catch (Exception e) {
//            log.info(e.getMessage());
//        }

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

    @Override
    public void updateProfileImage(String id, MultipartFile file) throws Exception {

        // 파일 저장 로직 구현
        String fileName = saveFile(file);

        // Auth 정보 업데이트
        Auth auth = authRepository.findById(id)
                .orElseThrow(() -> new Exception("Auth not found"));
        auth.setProfileImage(fileName);
        authRepository.save(auth);
    }

    private String saveFile(MultipartFile file) throws Exception {
        String originalName = file.getOriginalFilename();
        String fileName = System.currentTimeMillis() + "_" + originalName;
        String savePath = System.getProperty("user.dir") + "/src/main/resources/static/images/";
        if (!new File(savePath).exists()) {
            new File(savePath).mkdir();
        }
        String filePath = savePath + fileName;
        file.transferTo(new File(filePath));
        return fileName;
    }
}
