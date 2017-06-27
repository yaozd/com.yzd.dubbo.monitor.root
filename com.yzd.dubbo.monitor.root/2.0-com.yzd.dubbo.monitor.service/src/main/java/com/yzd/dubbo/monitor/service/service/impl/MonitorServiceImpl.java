package com.yzd.dubbo.monitor.service.service.impl;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.monitor.MonitorService;
import com.yzd.dubbo.monitor.common.seriazle.FSTSeriazle;

import java.util.List;

/**
 * Created by zd.yao on 2017/6/26.
 */
public class MonitorServiceImpl implements MonitorService {
    private static final Logger logger = LoggerFactory.getLogger(MonitorServiceImpl.class);
    @Override
    public void collect(URL url) {
        logger.info(String.valueOf(FSTSeriazle.serialize(url)));
        logger.info(url.toFullString());
    }

    @Override
    public List<URL> lookup(URL url) {
        return null;
    }
}
