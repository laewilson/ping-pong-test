package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class PongServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PongServiceApplication.class, args);
        log.info("Pong service started");
    }
}