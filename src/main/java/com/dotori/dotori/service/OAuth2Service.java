package com.dotori.dotori.service;

import com.dotori.dotori.dto.AuthDTO;
import com.dotori.dotori.dto.AuthSecurityDTO;
import com.dotori.dotori.entity.Auth;
import com.dotori.dotori.entity.OAuthAttributes;
import com.dotori.dotori.repository.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AuthRepository authRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();


        AuthDTO authDTO = OAuthAttributes.extract(registrationId, attributes);
        authDTO.setProvider(registrationId);

        Auth auth = updateOrSaveUser(authDTO);

        return (OAuth2User) new AuthSecurityDTO(
                auth.getAid(),
                auth.getId(),
                auth.getPassword(),
                auth.getNickName(),
                auth.getEmail(),
                auth.isSocial(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
    public Map getCustomAttribute(String registrationId,
                                  String userNameAttributeName,
                                  Map<String, Object> attributes,
                                  AuthDTO authDTO) {
        Map<String, Object> customAttribute = new ConcurrentHashMap<>();

        customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
        customAttribute.put("provider", registrationId);
        customAttribute.put("name", authDTO.getNickName());
        customAttribute.put("email", authDTO.getEmail());

        return customAttribute;
    }

    public Auth updateOrSaveUser(AuthDTO authDTO) {
        return authRepository.findByEmail(authDTO.getEmail())
                .map(entity -> updateExistingUser(entity, authDTO))
                .orElseGet(() -> createNewUser(authDTO));
    }


    public Auth updateExistingUser(Auth existingAuth, AuthDTO authDTO) {
        existingAuth.updateUser(authDTO.getNickName(), authDTO.getEmail());
        return authRepository.save(existingAuth);
    }

    public Auth createNewUser(AuthDTO authDTO) {
        Auth newAuth = authDTO.toEntity();
        return authRepository.save(newAuth);
    }
}
