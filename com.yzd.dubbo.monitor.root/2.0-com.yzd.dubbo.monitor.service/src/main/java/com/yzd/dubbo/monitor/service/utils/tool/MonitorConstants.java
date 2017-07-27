package com.yzd.dubbo.monitor.service.utils.tool;

import com.alibaba.dubbo.common.utils.ConfigUtils;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zd.yao on 2017/7/14.
 */
public class MonitorConstants {
    public static final String DUBBO_APPLICATION_NAME = ConfigUtils.getProperty("dubbo.application.name");
    public static final String DUBBO_REGISTRY_GROPU = ConfigUtils.getProperty("dubbo.registry.group");
    public static final String DUBBO_REGISTRY_ADDRESS = ConfigUtils.getProperty("dubbo.registry.default.address");
    public static final String OWNER = "owner";
    public static final String ORGANICATION = "organization";

    public static final String SESSION_USER_NAME = "SESSION_USER_NAME";

    public static final String SUCCESS = "success";
    public static final String FAIL = "fail";
    /**=============ip 所在的服务器===================================**/

    //存所有的----ip：name，例如 10.0.0.1：本地服务器
    public static final Map<String,String> ecsMap = new HashMap<>();
    // 双向map---内网 ip:外网 ip
    public static final BiMap<String,String> ecsBiMap = HashBiMap.create();
    //测试环境的ip---内网 ip:外网 ip
    public static final Map<String,String> ecsTestMap = new HashMap<>();
}
