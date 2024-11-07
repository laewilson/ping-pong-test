package org.example.common.limiter.algorithm;


import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

public class CrossThreadRateLimiter extends AbstractRateLimiter implements IRateLimiter {

    private final RateLimiter rateLimiter;

    public CrossThreadRateLimiter(long permits) {
        this.rateLimiter = RateLimiter.create(permits, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean tryAcquire() {
        return this.rateLimiter.tryAcquire();
    }
}
