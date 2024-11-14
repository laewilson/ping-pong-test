package org.example.ping;

import lombok.extern.slf4j.Slf4j;
import org.example.common.limiter.algorithm.IRateLimiter;
import org.example.common.limiter.algorithm.RateLimiterFactory;
import org.example.common.limiter.algorithm.RateLimiterType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.time.Duration;

//ping service
@EnableScheduling
@Component
@Slf4j
public class PingService {
    //web client for ping
    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = WebClient.create("http://localhost:7071");
    }

    // ping pong
    @Scheduled(fixedRate = 1000)
    public String pingPong() {
        try {
            // get rate limiter and acquire lock
            IRateLimiter rateLimiter = RateLimiterFactory.
                    create(RateLimiterType.CROSS_PROCESS, 2, "ping-service");
            if (rateLimiter.tryAcquire()) {
//                log.info("----> get Lock success");
                // got lock and can send request
                String port = System.getProperty("server.port");
                String result = webClient.get()
                        .uri("/pong/hello?clientId=" + port)
                        .exchangeToMono(clientResponse -> {
                            if (clientResponse.statusCode().is2xxSuccessful()) {
                                return clientResponse.bodyToMono(String.class).doOnNext(
                                        body -> log.info("=== [PING RESULT] Request sent & Pong Respond: {}", body));
                            } else if (clientResponse.statusCode().equals(HttpStatus.TOO_MANY_REQUESTS)) {
                                log.error("=== [PING RESULT] Request send & Pong throttled it.");
//                                return Mono.error(new RuntimeException("Pong service is throttled"));
                                return Mono.justOrEmpty("Pong service is throttled");

                            } else {
                                log.error("=== [PING RESULT] Unexpected response from Pong service: {}", clientResponse.statusCode());
                                return Mono.justOrEmpty("Unexpected response from Pong service");
                            }
                        }).block();
                       /* .subscribe(
                                response -> {},
                                error -> log.error("Request send & Pong throttled it: {}", error.getMessage())
                        )*/
                ;
                return result;

            } else {
                // did not get lock and cannot send request
                log.warn("=== [PING RESULT] Request not send as being rate limited.");
            }
        } catch (Exception e) {
            log.error("Error while trying to ping Pong service: ", e);
        }
        return null;
    }
}