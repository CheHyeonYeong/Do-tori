package com.dotori.dotori.config;

import com.dotori.dotori.security.CustomUserDetailsService;
import com.dotori.dotori.security.handler.Custom403Handler;
import com.dotori.dotori.security.handler.CustomSocialLoginSuccessHandler;
import com.dotori.dotori.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity
public class CustomSecurityConfig {

    private final DataSource dataSource;
    private final CustomUserDetailsService userDetailsService;
    private final OAuth2Service oAuth2Service;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        log.info("configure -> Security FilterChain");

        // 권한 설정
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/todo/**").permitAll()
                .requestMatchers("/assets/**").permitAll()
                .requestMatchers("/images/**").permitAll()
                .requestMatchers("/error/**").permitAll()
                .anyRequest().authenticated()
        );

        // 사용자 로그인 페이지
        http.formLogin(form -> {
            form.loginPage("/auth/login") // default login page가 아닌 다른 페이지로 바꿔주게끔 설정
                    .loginProcessingUrl("/auth/login") // 로그인 폼이 제출되는 URL 설정
                    .defaultSuccessUrl("/todo/list") // 로그인 성공 후 기본으로 리다이렉트될 URL 설정
                    .permitAll(); // 로그인 페이지에 대한 접근 허용
        });

        // 로그아웃 기능
        http.logout(logout -> logout
                .logoutUrl("/auth/logout") // 로그아웃 요청 URL
                .logoutSuccessUrl("/auth/login?logout") // 로그아웃 성공 후 리다이렉트 URL
                .invalidateHttpSession(true) // 로그아웃 시 세션 무효화
                .deleteCookies("JSESSIONID") // 로그아웃 시 JSESSIONID 쿠키 삭제
        );

        //csrf 설정
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        //remember-me 설정
        http.rememberMe(rememberMe ->
                rememberMe.key("123456789") // 세션에 저장해서 작업할 수 있어야 remember 되기 때문이다.
                        .rememberMeParameter("rememberMe") // 자동 로그인 체크박스의 name 속성 값
                        .tokenValiditySeconds(60 * 60 * 24 * 365) // 1년 : 60 * 60 * 24 * 365
                        .userDetailsService(userDetailsService) // 사용자 정보 서비스 설정
                        .authenticationSuccessHandler(authenticationSuccessHandler()) // 인증 성공 핸들러 설정
        );


        //exception Handler 설정
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
            httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler());
        });



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
        repo.setDataSource(dataSource);         // 통신을 위함
        return repo;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new DefaultOAuth2UserService();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CustomSocialLoginSuccessHandler(passwordEncoder);
    }
}