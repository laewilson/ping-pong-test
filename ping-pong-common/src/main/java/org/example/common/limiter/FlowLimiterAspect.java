package org.example.common.limiter;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.common.limiter.algorithm.IRateLimiter;
import org.example.common.limiter.algorithm.RateLimiterFactory;
import org.example.common.uitl.StrUtil;
import org.example.common.uitl.WebUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Aspect
@Component
public class FlowLimiterAspect {

    Map<String, IRateLimiter> limiterMap = new ConcurrentHashMap<>();
    @PostConstruct
    public void init() {
     log.info("=====>> init flow limiter");
    }

    @Around("@annotation(org.example.common.limiter.FlowLimiter)")
    public Object doAroundCache(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        FlowLimiter annotation = signature.getMethod().getAnnotation(FlowLimiter.class);
        if (annotation == null) {
            return joinPoint.proceed();
        }
        String name = annotation.name();
        if(StrUtil.isEmpty(name) ){
            name = signature.getDeclaringType().getName() +"." +signature.getMethod().getName();
        }
        IRateLimiter limiter = getLimiter(name, annotation);
        if (!limiter.tryAcquire()) {
            if (annotation.throwEx()) {
                throw new FlowLimitException(annotation.failedMsg());
            }
            log.warn("request flow limiter not passed , method:{}", name);
            setResponse(annotation);
            return Mono.justOrEmpty(annotation.failedMsg());
        }
//        log.info("request flow limiter passed, method:{}", name);
        return joinPoint.proceed();
    }
    private void setResponse(FlowLimiter annotation) {
      HttpServletResponse response =  WebUtil.getResponse();
      response.setHeader("X-RateLimit-Limit", String.valueOf(annotation.value()));
      response.setStatus(429);
      response.setHeader("Retry-After", "1");
    }
    private IRateLimiter getLimiter(String key, FlowLimiter annotation) {
        if (limiterMap.containsKey(key)) {
            return limiterMap.get(key);
        }
        IRateLimiter limiter =  RateLimiterFactory.create(annotation.type(), annotation.value(),annotation.name());
        limiterMap.put(key, limiter);

        return limiter;
    }


}
