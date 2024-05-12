package com.dotori.dotori.util;

import com.dotori.dotori.config.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@Log4j2
public class JWTUtilTest {

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    public void testGenerateToken() {
        Map<String, Object> claimMap = Map.of("id", "abced");
        String jwtstr = jwtUtil.generateToken(claimMap,10);
        log.info(jwtstr);
    }

    @Test
    public void testValidate(){
        String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ImFiY2VkIiwiaWF0IjoxNzE1NTE2MDYzLCJleHAiOjE3MTU1NTIwMDN9.-IcgoOfVqKKKy0WJ_7oWOoqVg21DAyIgUEpd7cSnOwU";
        Map<String, Object> claimMap = jwtUtil.validateToken(jwtStr);
        log.info(claimMap);
    }

    @Test
    public void testAll(){

        //다른 정보를 집어넣고 불러오는 것도 가능하다.
        String jwtStr = jwtUtil.generateToken(Map.of("id", "abced", "email", "aaaa@bbb.com"),10);
        log.info(jwtStr);

        Map<String, Object> claim = jwtUtil.validateToken(jwtStr);
        log.info("MID:"+claim.get("id"));
        log.info("EMAIL:"+claim.get("email"));
    }
}
