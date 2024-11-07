package org.example.common.limiter.algorithm;

import lombok.Data;

@Data
public class AbstractRateLimiter {

    private  long maxRequestsPerSecond = 5; // 每秒最大请求数

}
