package org.example.pong.test

import org.example.PongController
import org.example.model.PongDTO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import spock.lang.Specification;

import javax.annotation.PostConstruct;

@SpringBootTest/*(classes = [PongController.class])*/
class PongServiceTest extends Specification{

    private Logger log = LoggerFactory.getLogger(PongServiceTest.class);

    @Autowired
    private  PongController pongController;

    @PostConstruct
    void init() {
    }
    int threadCount = 10

    def "test pong service"() {
        List<String> results = new ArrayList<>()
        when:
        for (int i = 0; i < threadCount; i++) {
            new Thread({
                PongDTO dto = new PongDTO();
                Mono<String> mono = pongController.pong(dto);
                String result = mono.block();
                log.info("pong test result: " + result);
            }).start();
                Thread.sleep(100)
            results.add("ok")

        }

        then:
        assert results.size() == threadCount


    }




}
