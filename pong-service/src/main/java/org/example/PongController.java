package org.example;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.example.common.limiter.FlowLimiter;
import org.example.common.uitl.WebUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/pong")
public class PongController {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static final Config config = new Config();

    // 创建Hazelcast客户端
//    @Autowired
    private final HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance(config);

    @FlowLimiter(name="hello", value = 1,throwEx = false)
    @GetMapping("/hello")
    public Mono<?> pong() {

//        Map<String, String> map = new HashMap<>();
//        map.put("hello", "world");
//        map.put("time", simpleDateFormat.format(new Date()));
        String dateStr;
        log.info("hello world: {}", dateStr = simpleDateFormat.format(new Date()));
        Map<String, String> log = new HashMap<>();
        log.put("hello", "world");
        log.put("time", dateStr);
        log.put("ip", WebUtil.getUserIp());
        hazelcast.getList("hello").add(log);

        return Mono.justOrEmpty("world");
    }
    @GetMapping("/log-list")
    public List getLogList() {
        hazelcast.getList("hello").forEach(System.out::println);
        return hazelcast.getList("hello");
    }


}
