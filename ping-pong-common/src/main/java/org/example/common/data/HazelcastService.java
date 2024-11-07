package org.example.common.data;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class HazelcastService {
    private static final Config config = new Config();

    // 创建Hazelcast客户端
    private final HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance(config);


    @PostConstruct
    public void init() {

    }
    public HazelcastInstance getInstance(String name) {
        return null;
    }
    public HazelcastInstance getInstance() {
        return hazelcast;
    }




}
