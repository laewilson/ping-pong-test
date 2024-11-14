package org.example.common.limiter.algorithm;

public enum RateLimiterType {
    // limiter only cross thread
    CROSS_THREAD,
    // limiter can cross process
    CROSS_PROCESS,
    // limiter can cross machine (to be implemented)
    CROSS_MACHINE
}
