package com.dotori.dotori.entity;

import com.dotori.dotori.dto.AuthDTO;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {



    GOOGLE("google", (attribute) -> {
        AuthDTO authDTO = new AuthDTO();

        authDTO.setNickName((String)attribute.get("name"));
        authDTO.setEmail((String)attribute.get("email"));

        return authDTO;
    }),

    NAVER("naver", (attribute) -> {
        AuthDTO authDTO = new AuthDTO();

        Map<String, String> responseValue = (Map)attribute.get("response");

        authDTO.setNickName(responseValue.get("name"));
        authDTO.setEmail(responseValue.get("email"));

        return authDTO;
    });

    private final String registrationId; // 로그인한 서비스(ex) google, naver..)
    private final Function<Map<String, Object>, AuthDTO> of; // 로그인한 사용자의 정보를 통하여 UserProfile을 가져옴

    OAuthAttributes(String registrationId, Function<Map<String, Object>, AuthDTO> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static AuthDTO extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(value -> registrationId.equals(value.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}