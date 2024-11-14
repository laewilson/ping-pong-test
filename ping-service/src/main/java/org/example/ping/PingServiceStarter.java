package org.example.ping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class PingServiceStarter {

    public static void main(String[] args) {
        System.setProperty("server.port", String.valueOf(6002));
        SpringApplication.run(PingServiceStarter.class, args);
    }


}
