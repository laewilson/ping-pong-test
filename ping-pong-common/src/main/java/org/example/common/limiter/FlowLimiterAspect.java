package org.example.common.limiter;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.common.data.HazelcastService;
import org.example.common.limiter.algorithm.IRateLimiter;
import org.example.common.limiter.algorithm.RateLimiterFactory;
import org.example.common.uitl.StrUtil;
import org.example.common.uitl.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Aspect
@Component
public class FlowLimiterAspect {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Autowired
    private HazelcastService hazelcastService;
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
        if (StrUtil.isEmpty(name)) {
            name = signature.getDeclaringType().getName() + "." + signature.getMethod().getName();
        }
        IRateLimiter limiter = getLimiter(name, annotation);
        if (!limiter.tryAcquire()) {
            doLog(signature,true);
            if (annotation.throwEx()) {
                throw new FlowLimitException(annotation.failedMsg());
            }
            log.warn("request flow limiter not passed , method:{}", name);
            setResponse(annotation);
            return Mono.justOrEmpty(annotation.failedMsg());
        }
//        log.info("request flow limiter passed, method:{}", name);
        doLog(signature,false);
        return joinPoint.proceed();
    }

    private void doLog(MethodSignature signature, boolean isLimited) {
        Map<String, String> log = new HashMap<>();
        log.put("time", simpleDateFormat.format(new Date()));
        log.put("limited", String.valueOf(isLimited));
        log.put("method", signature.getDeclaringType().getName() + "." + signature.getMethod().getName());
        log.put("ip", WebUtil.getUserIp());
        hazelcastService.getInstance().getList(LimiterConstants.LOG_DATA_KEY).add(log);
    }
    private void setResponse(FlowLimiter annotation) {
        HttpServletResponse response = WebUtil.getResponse();
        if (response == null) {
            return;
        }
        response.setHeader("X-RateLimit-Limit", String.valueOf(annotation.value()));
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setHeader("Retry-After", "1");
    }

    private IRateLimiter getLimiter(String key, FlowLimiter annotation) {
        if (limiterMap.containsKey(key)) {
            return limiterMap.get(key);
        }
        IRateLimiter limiter = RateLimiterFactory.create(annotation.type(), annotation.value(), annotation.name());
        limiterMap.put(key, limiter);

        return limiter;
    }



}
