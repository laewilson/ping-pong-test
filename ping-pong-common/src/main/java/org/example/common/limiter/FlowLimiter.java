package org.example.common.limiter;

import org.example.common.limiter.algorithm.RateLimiterType;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface  FlowLimiter {

    /*
     * 限流器名称, 默认为方法名
     */
    String name() default "";
    /*
     * 限流器阈值
     */
    int value() default 10;
    /*
     * 限流器失败后的返回信息
     */
    String failedMsg() default "System busy, Please try again later";

    boolean throwEx() default true;

    /**
     * 限流器类型
     * @return
     */
    RateLimiterType type() default RateLimiterType.CROSS_THREAD;
}
