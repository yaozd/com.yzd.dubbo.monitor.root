package com.yzd.dubbo.monitor.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by zd.yao on 2017/6/26.
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    @RequestMapping(value = {"", "/index"})
    public String Index(){
        //return "home/index";
        return "forward:/application/index";
    }
}
