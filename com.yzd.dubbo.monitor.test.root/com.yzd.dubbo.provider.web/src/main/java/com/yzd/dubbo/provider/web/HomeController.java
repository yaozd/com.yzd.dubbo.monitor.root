package com.yzd.dubbo.provider.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zd.yao on 2017/6/28.
 */
@RestController
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @RequestMapping(value = {"", "/index"})
    public String Index(){
        logger.debug("home/index");
        logger.info("home/index");
        return "com.yzd.dubbo.provider.web-home/index";
    }
}
