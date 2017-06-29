package com.yzd.dubbo.monitor.service.service.impl;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.monitor.MonitorService;
import com.yzd.dubbo.monitor.common.constant.PubConfig;
import com.yzd.dubbo.monitor.common.seriazle.FSTSeriazle;
import com.yzd.dubbo.monitor.common.seriazle.FastJsonUtil;
import com.yzd.dubbo.monitor.common.stringExt.StringUtil;
import com.yzd.dubbo.monitor.common.timeExt.TimeUtil;
import com.yzd.dubbo.monitor.common.uuid.UUIDGenerator;
import com.yzd.dubbo.monitor.service.dao.entity.InvokeDO;
import com.yzd.frame.common.mq.redis.sharded.ShardedRedisMqUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zd.yao on 2017/6/26.
 */
public class MonitorServiceImpl implements MonitorService {
    private static final Logger logger = LoggerFactory.getLogger(MonitorServiceImpl.class);
    private static final String COUNT_PROTOCOL = "count";

    @Override
    public void collect(URL statistics) {
        logger.info(String.valueOf(FSTSeriazle.serialize(statistics)));
        logger.info(String.valueOf(FastJsonUtil.serialize(statistics)));
        logger.info(statistics.toFullString());
        if (statistics == null) return;
        if (!COUNT_PROTOCOL.equals(statistics.getProtocol())) {
            logger.info("COUNT-PROTOCOL IS NOT:" + String.valueOf(FastJsonUtil.serialize(statistics)));
            return;
        }
        InvokeDO dubboInvoke = toInvokeDO(statistics);
        //todo 调试代码
        logger.info(String.valueOf(FastJsonUtil.serialize(dubboInvoke)));
        if (dubboInvoke == null) return;
        ShardedRedisMqUtil redisUtil = ShardedRedisMqUtil.getInstance();
        redisUtil.lpush(PubConfig.MonitorListKey, StringUtil.byteToString(FSTSeriazle.serialize(dubboInvoke)));
    }

    @Override
    public List<URL> lookup(URL url) {
        return null;
    }

    public static InvokeDO toInvokeDO(URL statistics) {
        InvokeDO dubboInvoke = new InvokeDO();
        dubboInvoke.setUuId(UUIDGenerator.getUUID());
        dubboInvoke.setApplication(statistics.getParameter(APPLICATION, ""));
        dubboInvoke.setService(statistics.getServiceInterface());
        dubboInvoke.setMethod(statistics.getParameter(METHOD));
        dubboInvoke.setInvokeTime(statistics.getParameter(TIMESTAMP, System.currentTimeMillis()));
        dubboInvoke.setSuccess(statistics.getParameter(SUCCESS, 0));
        dubboInvoke.setFailure(statistics.getParameter(FAILURE, 0));
        dubboInvoke.setElapsed(statistics.getParameter(ELAPSED, 0));
        dubboInvoke.setConcurrent(statistics.getParameter(CONCURRENT, 0));
        dubboInvoke.setMaxElapsed(statistics.getParameter(MAX_ELAPSED, 0));
        dubboInvoke.setMaxConcurrent(statistics.getParameter(MAX_CONCURRENT, 0));
        //
        if (dubboInvoke.getSuccess() == 0 && dubboInvoke.getFailure() == 0 && dubboInvoke.getElapsed() == 0
                && dubboInvoke.getConcurrent() == 0 && dubboInvoke.getMaxElapsed() == 0 && dubboInvoke.getMaxConcurrent() == 0) {
            return null;
        }
        setAppTypeInfo(statistics, dubboInvoke);
        setAppTimeInfo(statistics, dubboInvoke);
        //
        return dubboInvoke;
    }
    //设置时间信息
    private static void setAppTimeInfo(URL statistics, InvokeDO dubboInvoke) {
        String timestamp = statistics.getParameter(Constants.TIMESTAMP_KEY);
        Date now;
        if (timestamp == null || timestamp.length() == 0) {
            now = new Date();
        } else if (timestamp.length() == "yyyyMMddHHmmss".length()) {
            try {
                now = new SimpleDateFormat("yyyyMMddHHmmss").parse(timestamp);
            } catch (ParseException e) {
                throw new IllegalStateException(e);
            }
        } else {
            now = new Date(Long.parseLong(timestamp));
        }
        String date = TimeUtil.getDateString(now);
        String hour = TimeUtil.getHourString(now);
        dubboInvoke.setInvokeDate(date);
        dubboInvoke.setInvokeHour(hour);
    }
    //设置ip地址，调用者类型信息
    private static void setAppTypeInfo(URL statistics, InvokeDO dubboInvoke) {
        final String consumerPort="0";
        if (statistics.getPort() == 0) {
            dubboInvoke.setAppType(CONSUMER);
            dubboInvoke.setConsumerHost(statistics.getHost());
            dubboInvoke.setConsumerPort(consumerPort);
            String provider = statistics.getParameter(PROVIDER);
            int i = provider.indexOf(':');
            if (i > 0) {
                String[] providerArray = provider.split(":");
                dubboInvoke.setProviderHost(providerArray[0]);
                dubboInvoke.setProviderPort(providerArray[1]);
            }
            return;
        }
        //
        dubboInvoke.setAppType(PROVIDER);
        dubboInvoke.setProviderHost(statistics.getHost());
        dubboInvoke.setProviderPort(String.valueOf(statistics.getPort()));
        dubboInvoke.setConsumerHost(statistics.getParameter(CONSUMER));
        dubboInvoke.setConsumerPort(consumerPort);

    }
}
