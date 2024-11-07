package org.example;

import lombok.extern.slf4j.Slf4j;
import org.example.common.data.FlowLimiterLogService;
import org.example.common.limiter.FlowLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/pong")
public class PongController {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    @Autowired
    private FlowLimiterLogService flowLimiterLogService;

    @FlowLimiter(name = "hello", value = 1, throwEx = false)
    @GetMapping("/hello")
    public Mono<?> pong() {

//        Map<String, String> map = new HashMap<>();
//        map.put("hello", "world");
//        map.put("time", simpleDateFormat.format(new Date()));
        String dateStr;
        log.info("hello world: {}", dateStr = simpleDateFormat.format(new Date()));

        return Mono.justOrEmpty("world");
    }
    @GetMapping("/log-list")
    public List getLogList() {
        return flowLimiterLogService.getLogList();
    }


}
