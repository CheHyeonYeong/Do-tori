package com.dotori.dotori.config.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class JWTUtil {

    @Autowired
    private Environment env;

    //token 생성 메서드
    public String generateToken(Map<String, Object> valueMap, int days) {
        log.info("enter generateToken");

        //문자열 key값인 jwt token을 제공한다
        Map<String ,Object> headers = new HashMap<>();

        //헤더
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");        //알고리즘은 HS256

        //페이로드 생성
        Map<String ,Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);

        //토큰 생성시간 설정
        int time = (60) * days;     // 일단은 1시간

        String key = env.getProperty("com.dotori.jwt.secret");

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(Date.from(ZonedDateTime.now().plusMinutes(1).toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant()))//header와 같게 맞춰줘야 한다!!
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();

        return jwtStr;
    }

    // token 검증 메서드
    public Map<String, Object> validateToken(String token) throws JwtException {
        Map<String, Object> claim = null;
        String key = env.getProperty("com.dotori.jwt.secret");
        claim = Jwts.parser().setSigningKey(key.getBytes()).build()     // Set Key
                .parseClaimsJws(token)
                .getBody();
        return claim;
    }
}