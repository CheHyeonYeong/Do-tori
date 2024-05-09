package com.dotori.dotori.security;

import com.dotori.dotori.dto.UserSecurityDTO;
import com.dotori.dotori.entity.User;
import com.dotori.dotori.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername : " + username);

        Optional<User> result = userRepository.getWithRoles(username);

        if(result.isEmpty()) {
            throw new UsernameNotFoundException("username not found....");
        }

        User user = result.get();

        UserSecurityDTO userSecurityDTO = new UserSecurityDTO(
                user.getUid(),
                user.getPassword(),
                user.getNickName(),
                user.getEmail(),
                user.isDel(),
                false,
                user.getRoleSet().stream().map(userRole ->
                        new SimpleGrantedAuthority("ROLE_" + userRole.name()))
                        .collect(Collectors.toList())
        );

        log.info("userSecurityDTO");
        log.info(userSecurityDTO);

        return userSecurityDTO;
    }
}
