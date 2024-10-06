package com.varietycrew.redis.controller;

import com.varietycrew.redis.Service.MemberService;
import com.varietycrew.redis.dto.SignInDTO;
import com.varietycrew.redis.security.JwtToken;
import com.varietycrew.redis.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    /* "members/sign-in" -> 모든 사용자에게 허용 */
    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody SignInDTO signInDto) {
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();
        log.info("*** username: {}, password: {}", username, password);

        JwtToken jwtToken = memberService.signIn(username, password);

        log.info("*** jwtToken = {}", jwtToken);
        log.info("*** request username = {}, password = {}", username, password);
        log.info("*** jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    /* "members/test" ->  USER 권한을 가진 사용자에게 허용 */
    @PostMapping("/test")
    public String test() {
        return SecurityUtil.getCurrentUsername();
    }

    @PostMapping("/health")
    public String health() {
        return "health";
    }

}