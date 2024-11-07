package org.example.common.limiter.algorithm;

public interface IRateLimiter {


    boolean tryAcquire();

}
