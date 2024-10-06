//package com.varietycrew.redis.Service;
//
//import com.varietycrew.redis.security.JwtToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//
//public interface MemberService extends UserDetailsService {
//    JwtToken signIn(String username, String password);
//}


package com.varietycrew.redis.Service;

import com.varietycrew.redis.repository.MemberRepository;
import com.varietycrew.redis.security.JwtToken;
import com.varietycrew.redis.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    //    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public JwtToken signIn(String username, String password) {
        log.info("*** MemberService - signIn : START");

        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        log.info("*** MemberService - signIn 1. authenticationToken: {}", authenticationToken);

        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        log.info("*** MemberService - signIn 2. authentication: {}", authentication);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        log.info("*** MemberService - signIn 3. jwtToken: {}", jwtToken);
        return jwtToken;
        
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}