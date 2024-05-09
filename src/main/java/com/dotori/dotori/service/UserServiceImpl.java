package com.dotori.dotori.service;

import com.dotori.dotori.dto.UserJoinDTO;
import com.dotori.dotori.entity.User;
import com.dotori.dotori.entity.UserRole;
import com.dotori.dotori.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public void join(UserJoinDTO userJoinDTO) throws UidExistException {
        String uid = userJoinDTO.getUid();

        boolean exists = userRepository.existsById(uid);

        if(exists) {
            throw new UidExistException();
        }

        User user = modelMapper.map(userJoinDTO, User.class);
        user.changePassword(passwordEncoder.encode(userJoinDTO.getPassword()));
        user.addRole(UserRole.USER);

        log.info("================");
        log.info(user);
        log.info(user.getRoleSet());

        userRepository.save(user);


    }
}
