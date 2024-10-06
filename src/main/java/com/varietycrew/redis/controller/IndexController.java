//package com.varietycrew.redis.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//@Slf4j
//@RequiredArgsConstructor
//public class IndexController {
////    private final UserRepository userRepository;
////    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    /* 회원가입 */
//    @GetMapping("/joinForm")
//    public String joinForm() {
//        return "joinForm";
//    }
//
////    @PostMapping("/join")
////    public String join(User user) {
////        user.setRole("USER");
////        String rawPassword = user.getPassword();
////        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
////        user.setPassword(encPassword);
////        userRepository.save(user);
////        log.info("user = {} ", user);
////        return "redirect:/loginForm";
////    }
//
//    /* 로그인 */
//    @GetMapping("/loginForm")
//    public String login() {
//        return "loginForm";
//    }
//}
