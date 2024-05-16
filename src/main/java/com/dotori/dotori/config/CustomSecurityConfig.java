package com.dotori.dotori.config;

import com.dotori.dotori.config.util.JWTUtil;
import com.dotori.dotori.security.CustomUserDetailsService;
import com.dotori.dotori.security.filter.LoginFilter;
import com.dotori.dotori.security.filter.TokenCheckFilter;
import com.dotori.dotori.security.handler.Custom403Handler;
import com.dotori.dotori.security.handler.LoginSuccessHandler;
import com.dotori.dotori.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

    private final DataSource dataSource;
    private final CustomUserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;
    private final OAuth2UserService oAuth2UserService;
    private final OAuth2Service oAuth2Service;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        log.info("configure -> Security FilterChain");

        // 사용자 로그인 페이지
        http.formLogin(form -> {
            form.loginPage("/auth/login"); // default login page가 아닌 다른 페이지로 바꿔주게끔 설정
        });

        // AuthenticationManagerBuilder 설정
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService);

        // Get AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // 인증 매니저 등록
        http.authenticationManager(authenticationManager);

        // API Login Filter 설정
        LoginFilter apiLoginFilter = new LoginFilter("/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);
        LoginSuccessHandler successHandler = new LoginSuccessHandler(jwtUtil);
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler);

        // APILoginFilter의 위치 조정 - UsernamePasswordAuthenticationFilter 이전에 동작해야 하는 필터이기 때문
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);

        // TokenCheckFilter 설정
        http.addFilterBefore(
                tokenCheckFilter(jwtUtil, userDetailsService),
                UsernamePasswordAuthenticationFilter.class
        );

        // OAuth2 로그인 설정
        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/auth/login")
                .defaultSuccessUrl("/todo/list")
                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService()))
        );

        // CSRF 토큰 비활성화
        http.csrf(csrf -> csrf.disable());

        // Remember-me 설정
        http.rememberMe(rememberMe -> {
            rememberMe.key("123456789")
                    .tokenRepository(persistentTokenRepository())
                    .userDetailsService(userDetailsService)
                    .tokenValiditySeconds(60 * 60 * 24 * 30); // 30일
        });

        // CORS 설정
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // Access Denied Handler 설정
        http.exceptionHandling(exceptionHandling -> exceptionHandling.accessDeniedHandler(accessDeniedHandler()));

        // OAuth2 로그인 설정
        http.oauth2Login(oauth2Login -> {
            oauth2Login.defaultSuccessUrl("/todo/list", true)
                    .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(oAuth2Service));
        });

        return http.build();
    }

    // 정적 자원들에 필터 적용 제외
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("----------web configure-----------");

        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }

    // TokenCheckFilter 객체 생성
    private TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil, CustomUserDetailsService userDetailService) {return new TokenCheckFilter(userDetailService, jwtUtil);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authentication", "Cache-Control", "Content-Type"));
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://your-frontend-domain.com")); // CORS 허용 도메인
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new DefaultOAuth2UserService();
    }

}