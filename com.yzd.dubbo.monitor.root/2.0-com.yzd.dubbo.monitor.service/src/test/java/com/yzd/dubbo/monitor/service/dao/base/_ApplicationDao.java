package com.yzd.dubbo.monitor.service.dao.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by zd.yao on 2017/6/30.
 */
@SpringBootApplication
@ComponentScan("com.yzd.dubbo.monitor.service.dao")
public class _ApplicationDao {
    public static void main(String[] args) {
        SpringApplication.run(_ApplicationDao.class, args);
    }
}