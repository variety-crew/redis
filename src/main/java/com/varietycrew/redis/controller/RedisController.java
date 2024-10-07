package com.varietycrew.redis.controller;

import com.varietycrew.redis.Service.RedisService;
import com.varietycrew.redis.dto.RedisDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RedisController {
    private final RedisService redisService;

    @GetMapping("/redis")
    public String getRedis(@RequestBody RedisDTO param) {
        return redisService.getRedis(param);
    }
}
