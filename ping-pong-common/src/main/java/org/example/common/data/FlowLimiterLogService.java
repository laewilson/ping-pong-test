package org.example.common.data;

import org.example.common.limiter.LimiterConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlowLimiterLogService {

    @Autowired
    private HazelcastService hazelcastService;

    public List getLogList(){
        return hazelcastService.getInstance().getList(LimiterConstants.LOG_DATA_KEY);
    }

}
