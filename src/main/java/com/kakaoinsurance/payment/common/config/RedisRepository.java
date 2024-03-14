package com.kakaoinsurance.payment.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Boolean lock(final String key) {
        return redisTemplate
            .opsForValue()
            .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3_000));
    }

    public Boolean unlock(final String key) {
        return redisTemplate.delete(generateKey(key));
    }

    private String generateKey(final String key) {
        return key.toString();
    }

}
