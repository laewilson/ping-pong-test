package org.example.test

import org.example.ping.PingService
import org.example.ping.PingServiceStarter
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ConfigurableApplicationContext
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@SpringBootTest(classes = [PingService.class])
class PingServiceTest extends Specification {


    List<ConfigurableApplicationContext> contextList = new ArrayList<>()
    // setup instance num
    int pingInstanceNum = 4


    def " start ping instances and observe"() {
        given: "the two application contexts are running"
        PollingConditions conditions = new PollingConditions(timeout: 100, initialDelay: 1)

        when: "checking if all applications are available"

        then: "they should be running and listening on their respective ports"
        conditions.eventually {
            assert contextList.size() == pingInstanceNum
        }
    }
    def setup() {
        // 启动多个实例 测试限流代码
        for (int i = 0; i < pingInstanceNum; i++) {
            int port = 8081 + i
            ConfigurableApplicationContext context =   new SpringApplicationBuilder()
                    .sources(PingServiceStarter.class) //
                    .profiles("default") //
                    .properties(["server.port": port])
                    .run()
            PingService pingService =  context.getBean("pingService") as PingService
            assert pingService != null
//            assert pingService.pingPong() != null
            contextList.add(context)
        }

    }
    def cleanup() {
        contextList.forEach {
            if(it?.isRunning()) {
                it?.close()
            }
        }
    }

}
