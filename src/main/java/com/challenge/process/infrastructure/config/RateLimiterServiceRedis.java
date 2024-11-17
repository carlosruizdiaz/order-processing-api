package com.challenge.process.infrastructure.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Si necesitas un sistema de rate limiting distribuido (por ejemplo, si tienes múltiples instancias de tu API),
 * puedes usar Redis para almacenar y controlar el número de solicitudes por usuario o IP.
 */
@Service
public class RateLimiterServiceRedis {
    private static final String RATE_LIMIT_KEY = "rate_limit:";
    private static final int MAX_REQUESTS_PER_SECOND = 5;
    private static final Duration WINDOW_DURATION = Duration.ofSeconds(1);

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean isRequestAllowed(String userId) {
        String key = RATE_LIMIT_KEY + userId;
        Long currentCount = redisTemplate.opsForValue().increment(key, 1);

        if (currentCount == 1) {
            redisTemplate.expire(key, WINDOW_DURATION);
        }

        return currentCount <= MAX_REQUESTS_PER_SECOND;
    }
}
