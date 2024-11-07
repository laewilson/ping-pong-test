package org.example.common.limiter.algorithm;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CrossMachineRateLimiter extends  AbstractRateLimiter implements  IRateLimiter{

    public CrossMachineRateLimiter(long permitsPerSecond) {
        setMaxRequestsPerSecond(permitsPerSecond);
    }

    @Override
    public boolean tryAcquire() {
        return false;
    }
}
