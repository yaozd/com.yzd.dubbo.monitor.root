package com.yzd.dubbo.monitor.service.service.impl;

import com.yzd.dubbo.monitor.service.service.inf.RedisServiceInf;
import com.yzd.frame.common.mq.redis.sharded.ShardedRedisMqUtil;
import org.springframework.stereotype.Service;

/**
 * Created by zd.yao on 2017/7/4.
 */
@Service
public class RedisServiceImpl implements RedisServiceInf {
    @Override
    public String brpop(int timeout, String key) {
        return ShardedRedisMqUtil.getInstance().brpop(timeout,key);
    }
}
