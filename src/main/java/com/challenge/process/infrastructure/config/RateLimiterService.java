package com.challenge.process.infrastructure.config;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {
    private final RateLimiter rateLimiter = RateLimiter.create(2.0);

    public boolean isRequestAllowed() {
        return rateLimiter.tryAcquire();
    }
}
