package com.dotori.dotori.security.filter;

import com.dotori.dotori.config.util.JWTUtil;
import com.dotori.dotori.security.exception.AccessTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    // 실질적 filter 생성 클래스

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // auth/api로 접근한 것이 아니라면 checkfilter로 사용하지 않고 다음 필터로 넘어감
        if(!path.startsWith("/auth/api")){
            filterChain.doFilter(request, response);
            return;
        }
        log.info("Token check filter");

        try {
            validateAccessToken(request);
            filterChain.doFilter(request, response);
        } catch (AccessTokenException e){
            e.sendResponseError(response);
        }
    }
    private Map<String, Object> validateAccessToken(HttpServletRequest request) {
        String headerStr = request.getHeader("Authorization");
        if(headerStr == null || !headerStr.startsWith("Bearer ") || headerStr.length() < 8){ //token is not defind
            throw new AccessTokenException(AccessTokenException.Token_ERROR.UNACCEPT);
        }
        // Bearer 생략
        String tokenType = headerStr.substring(0,6);
        String tokenStr = headerStr.substring(7);
        if(tokenType.equalsIgnoreCase("Bearer") == false){      // 잘못된 타입
            throw new AccessTokenException(AccessTokenException.Token_ERROR.BADTYPE);
        }

        try {
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);
            return values;
        }catch (MalformedJwtException e){   //기본적으로 만들어져 있는 것
            log.info("Invalid JWT token");
            throw new AccessTokenException(AccessTokenException.Token_ERROR.MALFORM);
        }catch (SignatureException e){
            log.info("Signature exception");
            throw new AccessTokenException(AccessTokenException.Token_ERROR.BADSIGN);
        }catch (ExpiredJwtException e){
            log.info("Expired JWT token");
            throw new AccessTokenException(AccessTokenException.Token_ERROR.EXPIRD);
        }

    }
}
