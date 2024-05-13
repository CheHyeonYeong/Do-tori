package com.dotori.dotori.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Log4j2
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    public LoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
    }

    private Map<String, String> parseRequstJSON(HttpServletRequest request) {

        try (Reader reader = new InputStreamReader(request.getInputStream()) ){
            Gson gson = new Gson();
            return gson.fromJson(reader, Map.class);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("APILoginFilter attemptAuthentication");

        if(request.getMethod().equalsIgnoreCase("GET")) {
            //get은 처리하지 않아요
            log.info("Get Method not support");
            return null;
        }

        Map<String, String> jsonData = parseRequstJSON(request);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(jsonData.get("id"), jsonData.get("password"));
        return getAuthenticationManager().authenticate(authenticationToken);

    }
}
