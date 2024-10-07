package com.varietycrew.redis.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RedisDTO {
    private String key;
    private String value;
}