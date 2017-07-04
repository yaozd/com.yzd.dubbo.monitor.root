package com.yzd.dubbo.monitor.service.service.inf;

/**
 * Created by zd.yao on 2017/7/4.
 */
public interface RedisServiceInf {
    String brpop(int timeout, String key);
}
