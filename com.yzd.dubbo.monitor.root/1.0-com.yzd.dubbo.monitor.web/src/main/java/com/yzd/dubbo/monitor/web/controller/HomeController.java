package com.yzd.dubbo.monitor.web.controller;

import com.alibaba.dubbo.common.URL;
import com.yzd.dubbo.monitor.common.seriazle.FastJsonUtil;
import com.yzd.dubbo.monitor.service.service.inf.RegistryServiceInf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

/**
 * Created by zd.yao on 2017/6/26.
 */
@RestController
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @RequestMapping(value = {"", "/index"})
    public String Index(){
        logger.debug("home/index");
        logger.info("home/index");
        return "home/index";
    }
    @Autowired
    RegistryServiceInf registryServiceInf;
    @RequestMapping("getRegistryCache")
    public Map<String, Map<String, Set<URL>>>  getRegistryCache(){
        Map<String, Map<String, Set<URL>>> result=registryServiceInf.getRegistryCache();
        return result;
    }
    @RequestMapping("getProvidersInRegistryCache")
    public  String getProviderInRegistryCache(){
        Map<String, Set<URL>>  providers=registryServiceInf.getRegistryCache().get("providers");
        String result= FastJsonUtil.serialize(providers);
        return result;
    }
}
