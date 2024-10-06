package com.varietycrew.redis.config;

import com.varietycrew.redis.security.JwtAuthenticationFilter;
import com.varietycrew.redis.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//@Configuration
//@EnableWebSecurity      // spring security 지원을 가능하게 함
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable()); // Security 6.1 버전 이상 csrf 비활성화 방법
//
//        http.authorizeHttpRequests()
//                .requestMatchers("/user/**").authenticated() // 로그인해야 들어올수 있음
//                .requestMatchers("/manager/**").hasRole("MANAGER'")
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .anyRequest().authenticated();
//        http.formLogin()
//                .loginPage("/loginForm")
//                .loginProcessingUrl("/login") // login 주소가 호출이되면 시큐리티가 낚아채서 대신 로그인 진행해줌.
//                .defaultSuccessUrl("/");
//        return http.build();
//    }
//}

//@Configuration
//@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨.
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable());
//
//        // 특정 URL에 대한 권한 설정
//        http.authorizeHttpRequests((authorizeRequests) -> {
//            // hasAnyRole()을 사용할 때 자동으로 ROLE_이 붙기 때문에 ROLE_ 붙이지 않음
//            authorizeRequests.requestMatchers("/user/**").authenticated()  // 로그인해서 들어올 수 있음
//                    .requestMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
//                    .requestMatchers("/admin/**").hasRole("ADMIN")
//                    .anyRequest().permitAll();
//                })
//                .formLogin((formLogin) -> { // 권한이 필요한 요청은 해당 url로 리다이렉트
//                    formLogin.loginPage("/loginForm")
//                            .loginProcessingUrl("/login")   // login 주소가 호출이 되면 시큐리티가 대신 로그인 진행
//                            .defaultSuccessUrl("/");
//                });
//
//        return http.build();
//    }
//}

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
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
                .requestMatchers("/members/sign-in").permitAll() // 해당 API에 대해서는 모든 요청을 허가
                .requestMatchers("/members/test").hasRole("USER") // USER 권한이 있어야 요청할 수 있음
                .anyRequest().authenticated() // 이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정
        );

        // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
        httpSecurity.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class).build();

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}