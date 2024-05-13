package com.dotori.dotori.config;

import com.dotori.dotori.config.util.JWTUtil;
import com.dotori.dotori.security.CustomUserDetailsService;
import com.dotori.dotori.security.filter.LoginFilter;
import com.dotori.dotori.security.filter.TokenCheckFilter;
import com.dotori.dotori.security.handler.Custom403Handler;
import com.dotori.dotori.security.handler.LoginSuccessHandler;
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

        // api login filter 설정
        LoginFilter apiLoginFilter = new LoginFilter("/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        // APILoginFilter의 위치 조정 - UsernamePasswordAuthenticationFilter 이전에 동작해야 하는 필터이기 때문
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);
        LoginSuccessHandler successHandler = new LoginSuccessHandler(jwtUtil);
        apiLoginFilter.setAuthenticationSuccessHandler(successHandler);

        // auth/api로 시작하는 모든 경로는 TokenCheckFilter 동작 => TokenCheckFilter에서 처리함
        LoginFilter loginFilter = new LoginFilter("/gengerateToken");
        loginFilter.setAuthenticationManager(authenticationManager);

        //APILoginFilter의 위치 조정 - UsernamepasswordAuthenctionFilter 이전에 동작해야 하는 필터이기 때문
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);

        // api로 시작하는 모든 경로는 TokenCheckFilter 동작
        http.addFilterBefore(
                tokenCheckFilter(jwtUtil, userDetailsService),
                UsernamePasswordAuthenticationFilter.class
        );

        // CSRF 토큰 비활성화
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        // remember-me 설정
        http.rememberMe(httpSecurityRememberMeConfigurer -> {
            httpSecurityRememberMeConfigurer.key("123456789")
                    .tokenRepository(persistentTokenRepository())
                    .userDetailsService(userDetailsService)
                    .tokenValiditySeconds(60 * 60 * 24 * 30); // 30일
        });

        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
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

    // token check filter 객체 생성
    private TokenCheckFilter tokenCheckFilter(JWTUtil jwtUtil, CustomUserDetailsService userDetailService){
        return new TokenCheckFilter(userDetailService, jwtUtil);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authentication", "Cache-Control", "Content-Type"));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource sorce = new UrlBasedCorsConfigurationSource();
        sorce.registerCorsConfiguration("/**", configuration);
        return sorce;
    }

}