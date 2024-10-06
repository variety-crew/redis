package com.varietycrew.redis.config;

import com.varietycrew.redis.security.JwtAuthenticationFilter;
import com.varietycrew.redis.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    /* 참고 블로그: https://suddiyo.tistory.com/entry/Spring-Spring-Security-JWT-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0-2 */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // REST API이므로 basic auth 및 csrf 보안을 사용하지 않음
        httpSecurity.httpBasic(httpBasic -> httpBasic.disable());
        httpSecurity.csrf(csrf -> csrf.disable());

        // JWT를 사용하기 때문에 세션을 사용하지 않음
        httpSecurity.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers("/members/sign-in").permitAll() // 로그인 페이지에 대해서는 모든 요청을 허가
                .requestMatchers("/members/test").hasRole("USER") // test 페이지는 USER 권한이 있어야 요청할 수 있음
                .requestMatchers("/members/health").permitAll()
                .anyRequest().authenticated() // 이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정
        );

        // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
        httpSecurity.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    // AuthenticationManager를 Bean으로 등록
    // AuthenticationManager는 사용자가 입력한 ID와 pw가 데이터베이스에 저장된 정보와 일치하는지 확인함
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}