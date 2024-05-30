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
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AuthRepository authRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // DefaultOAuth2UserService를 사용하여 OAuth2User 객체를 로드
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 클라이언트 등록 ID (구글, 네이버 등)를 가져옴
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 로그인 시 키(PK)가 되는 필드 값을 가져옴
        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 Map
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // registrationId에 따라 유저 정보를 통해 AuthDTO 객체 생성
        AuthDTO authDTO = OAuthAttributes.extract(registrationId, attributes);
        authDTO.setProvider(registrationId);

        try {
            // DB에 저장하거나 정보를 업데이트하고 AuthSecurityDTO 객체를 생성하여 반환
            Auth auth = updateOrSaveUser(authDTO);
            return (OAuth2User) new AuthSecurityDTO(
                    auth.getAid(),
                    auth.getId(),
                    auth.getPassword(),
                    auth.getNickName(),
                    auth.getEmail(),
                    auth.isSocial(),
                    auth.getProfileImage(),
                    auth.isTutorialDone(),
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
            );
        } catch (AuthService.MidExistException e) {
            // 중복된 사용자 ID 예외 처리
            OAuth2Error oauth2Error = new OAuth2Error("duplicate_user_id", "Duplicate user ID", null);
            throw new OAuth2AuthenticationException(oauth2Error, e.getMessage());
        } catch (AuthService.EmailExistException e) {
            // 중복된 이메일 예외 처리
            OAuth2Error oauth2Error = new OAuth2Error("duplicate_user_email", "Duplicate user email", null);
            throw new OAuth2AuthenticationException(oauth2Error, e.getMessage());
        } catch (AuthService.NickNameLengthException e) {
            // 닉네임 길이 예외 처리
            OAuth2Error oauth2Error = new OAuth2Error("invalid_nickname_length", "Nickname length should be less than or equal to 5", null);
            throw new OAuth2AuthenticationException(oauth2Error, e.getMessage());
        }

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

    public Auth updateOrSaveUser(AuthDTO authDTO) throws AuthService.MidExistException, AuthService.EmailExistException, AuthService.NickNameLengthException {
        return authRepository.findByEmail(authDTO.getEmail())
                .map(entity -> updateExistingUser(entity, authDTO))
                .orElseGet(() -> {
                    try {
                        // 새로운 사용자 생성
                        return createNewUser(authDTO);
                    } catch (AuthService.MidExistException | AuthService.EmailExistException | AuthService.NickNameLengthException e) {
                        // 예외를 RuntimeException으로 감싸서 다시 던짐
                        throw new RuntimeException(e);
                    }
                });
    }


    public Auth updateExistingUser(Auth existingAuth, AuthDTO authDTO) {
        existingAuth.updateUser(authDTO.getNickName(), authDTO.getEmail());
        return authRepository.save(existingAuth);
    }

    public Auth createNewUser(AuthDTO authDTO) throws AuthService.MidExistException, AuthService.EmailExistException, AuthService.NickNameLengthException {
        String email = authDTO.getEmail();
        String nickName = authDTO.getNickName();

        // 이메일 중복 검사
        if (authRepository.existsByEmail(email)) {
            throw new AuthService.EmailExistException("이미 존재하는 이메일입니다.");
        }

        // 닉네임 길이 검사
        if (nickName.length() > 5) {
            throw new AuthService.NickNameLengthException("닉네임은 5글자 이하여야 합니다.");
        }

        // AuthDTO를 Auth 엔티티로 변환하여 저장
        Auth newAuth = authDTO.toEntity();
        return authRepository.save(newAuth);
    }
}
