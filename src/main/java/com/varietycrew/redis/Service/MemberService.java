package com.varietycrew.redis.Service;

import com.varietycrew.redis.security.JwtToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MemberService extends UserDetailsService {
    JwtToken signIn(String username, String password);
}
