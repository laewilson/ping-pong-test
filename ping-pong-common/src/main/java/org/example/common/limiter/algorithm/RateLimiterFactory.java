package org.example.common.limiter.algorithm;

public class RateLimiterFactory {

    public static IRateLimiter create(long permitsPerSecond) {
        return new CrossThreadRateLimiter(permitsPerSecond);
    }

    public static IRateLimiter create(RateLimiterType rateLimiterType, long permitsPerSecond,String key) {
        if (rateLimiterType == RateLimiterType.CROSS_THREAD) {
            return new CrossThreadRateLimiter(permitsPerSecond);
        }
        if (rateLimiterType == RateLimiterType.CROSS_PROCESS) {
            return new CrossProcessRateLimiter(permitsPerSecond,key);
        }
        if (rateLimiterType == RateLimiterType.CROSS_MACHINE) {
            return new CrossThreadRateLimiter(permitsPerSecond);
        }
        throw new IllegalArgumentException("Unknown rate limiter type: " + rateLimiterType);
    }
}
