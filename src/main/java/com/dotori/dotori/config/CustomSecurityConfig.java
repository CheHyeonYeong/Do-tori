package com.dotori.dotori.config;

import com.dotori.dotori.security.CustomUserDetailsService;
import com.dotori.dotori.security.handler.Custom403Handler;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

    private final DataSource dataSource;
    private final UserDetailsService userDetailsService;

    // 로그인 페이지로 이동 안하고 바로 접근 가능

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("configure -> Secutrity FilterChian");

        //사용자 로그인 페이지
        http.formLogin(form -> {
            form.loginPage("/auth/login"); //default login page가 아닌 다른 페이지로 바꿔준다
        });
        //csrf 설정
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        //remember-me 설정
        http.rememberMe(httpSecurityRememberMeConfigurer -> {
            httpSecurityRememberMeConfigurer.key("123456789")           //DB에 저장해서 작업할 수 있어야 remember 되기 때문이다.
                    .tokenRepository(persistentTokenRepository())
                    .userDetailsService(userDetailsService)
                    .tokenValiditySeconds(60*60*24*30);     //30일
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


}