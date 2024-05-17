package com.dotori.dotori.security.filter;

import com.dotori.dotori.config.util.JWTUtil;
import com.dotori.dotori.security.CustomUserDetailsService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    // 실질적 filter 생성 클래스

    private final CustomUserDetailsService userDetailService;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("TokenCheckFilter");
        String path = request.getRequestURI();

        // auth/api로 접근한 것이 아니라면 checkfilter로 사용하지 않고 다음 필터로 넘어감
        if (!path.startsWith("/auth/api")) {
            log.info("TokenCheckFilter do not commin!!!");
            filterChain.doFilter(request, response);
            return;
        }
        log.info("Token check filter");

        try {
            // 액세스 토큰 검증
            Map<String, Object> payload = validateAccessToken(request);

            // id 추출
            String id = (String) payload.get("id");
            log.info("id : " + id);

            // UserDetails 조회
            UserDetails userDetails = userDetailService.loadUserByUsername(id);

            // 인증 토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            // SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // 다음 필터로 요청 전달
            filterChain.doFilter(request, response);

        } catch (AccessTokenException e) {
            // 액세스 토큰 검증 실패 시 오류 응답 전송
            e.sendResponseError(response);
        }
    }

    private Map<String, Object> validateAccessToken(HttpServletRequest request) {
        // 요청 헤더에서 Authorization 값 추출
        String headerStr = request.getHeader("Authorization");

        // 토큰이 없거나 형식이 올바르지 않은 경우
        if (headerStr == null || !headerStr.startsWith("Bearer ") || headerStr.length() < 8) {
            throw new AccessTokenException(AccessTokenException.Token_ERROR.UNACCEPT);
        }

        // 토큰 타입 및 토큰 값 추출
        String tokenType = headerStr.substring(0, 6);
        String tokenStr = headerStr.substring(7);

        // 토큰 타입이 Bearer가 아닌 경우
        if (!tokenType.equalsIgnoreCase("Bearer")) {
            throw new AccessTokenException(AccessTokenException.Token_ERROR.BADTYPE);
        }

        try {
            // 토큰 검증 및 페이로드 추출
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);
            return values;
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token");
            throw new AccessTokenException(AccessTokenException.Token_ERROR.MALFORM);
        } catch (SignatureException e) {
            log.info("Signature exception");
            throw new AccessTokenException(AccessTokenException.Token_ERROR.BADSIGN);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token");
            throw new AccessTokenException(AccessTokenException.Token_ERROR.EXPIRD);
        }
    }
}