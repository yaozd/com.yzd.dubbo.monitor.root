package com.yzd.dubbo.monitor.service.service.inf;

import com.alibaba.dubbo.common.URL;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by zd.yao on 2017/7/13.
 */
public interface RegistryServiceInf {
    //注册中心发布订阅服务
    void subscribe();
    //当前的所有服务
    Map<String, Map<String, Set<URL>>> getRegistryCache();
    //
    Date getFinalUpdateTime();
}
