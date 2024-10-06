package com.varietycrew.redis.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 0. 로그 출력 (테스트용)
        HttpServletRequest httpRequest = (HttpServletRequest) request;      // ServletRequest를 HttpServletRequest로 변환
        String requestURL = httpRequest.getRequestURL().toString();         // 요청 URL
        String requestMethod = httpRequest.getMethod();                     // 요청 메서드 (GET, POST 등)

        // 로그 출력
        log.info("*** doFilter - Request URL: {}", requestURL);
        log.info("*** doFilter - Request Method: {}", requestMethod);

        // 1. Request Header에서 JWT 토큰 추출
        log.info("*** doFilter - 1. Request Header에서 JWT 토큰 추출");
        String token = resolveToken((HttpServletRequest) request);

        // 2. validateToken으로 토큰 유효성 검사
        log.info("*** doFilter - 2. validateToken으로 토큰 유효성 검사");
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        log.info("*** doFilter - 3. chain.doFilter(request, response); 실행");
        chain.doFilter(request, response);
        log.info("*** doFilter - END");
    }

    // Request Header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}