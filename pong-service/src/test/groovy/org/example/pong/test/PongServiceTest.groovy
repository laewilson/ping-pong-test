//package org.example.pong.test
//
//import org.example.PongController
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.http.HttpStatus;
//import org.springframework.web.reactive.function.client.WebClient
//import reactor.core.publisher.Mono
//import spock.lang.Specification;
//
//import javax.annotation.PostConstruct;
//
//@SpringBootTest(classes = [PongController.class])
//class PongServiceTest extends Specification{
//
//    private Logger log = LoggerFactory.getLogger(PongServiceTest.class);
//
//    @Autowired
//    private  PongController pongController;
//    private WebClient webClient;
//
//    @PostConstruct
//    void init() {
//        this.webClient = WebClient.create("http://localhost:7071");
//    }
//
//    def "test pong service"() {
//
//        when:
//         String result = pongController.pong();
//        then:
//        result != null;
//
//
//    }
//
//
//
//
//}
