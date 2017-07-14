package com.yzd.dubbo.monitor.web.schedule;

import com.yzd.dubbo.monitor.common.constant.PubConfig;
import com.yzd.dubbo.monitor.common.seriazle.FSTSeriazle;
import com.yzd.dubbo.monitor.common.seriazle.FastJsonUtil;
import com.yzd.dubbo.monitor.common.stringExt.StringUtil;
import com.yzd.dubbo.monitor.service.dao.entity.InvokeDO;
import com.yzd.dubbo.monitor.service.service.inf.InvokeServiceInf;
import com.yzd.dubbo.monitor.service.service.inf.RedisServiceInf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by zd.yao on 2017/6/28.
 */
@Service
public class StatisticsJob {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsJob.class);
    /**
     * 使用batch insert解决MySQL的insert吞吐量问题
     * http://blog.csdn.net/orion61/article/details/32108547
     * 在我这个简单测试场景中，values number最合适的值是50，和单values对比，耗时减少 97% ，insert吞吐量提升 36倍 。
     * @param recordList
     */
    private static int BATCH_MAX_COUNT=50;//批处理插入默认为50或者阻塞5秒触发;
    @Autowired
    private RedisServiceInf redisServiceInf;
    @Autowired
    private InvokeServiceInf invokeServiceInf;
    @Scheduled(initialDelay = 3000, fixedDelay = 1000 * 15)
    public void saveData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        //logger.info("每隔15秒钟执行一次 " + dateFormat.format(new Date()));
        logger.info("启动批量导入redis消息队列中的数据到mysql数据库中任务 " + dateFormat.format(new Date()));
        List<InvokeDO> listInvokeDO;
        CountDownLatch latch;
        while (true) {
            listInvokeDO = new ArrayList<>();
            latch = new CountDownLatch(BATCH_MAX_COUNT);
            try {
                getDataList(listInvokeDO, latch);
                //todo 调试代码
                //logger.info("String.valueOf(listInvokeDO.size()):"+String.valueOf(listInvokeDO.size()));
            } catch (Exception e) {
                throw new IllegalStateException(e);
            } finally {
                if (listInvokeDO.size() > 0) {
                    //插入数据库
                    invokeServiceInf.addRecordBatch(listInvokeDO);
                    logger.info("===listInvokeDO.size()="+listInvokeDO.size());
                }
            }
        }
    }

    private void getDataList(List<InvokeDO> listUrl, CountDownLatch latch) {
        while (latch.getCount() > 0) {
            //尾部拉取阻塞时间为5秒
            String result = redisServiceInf.brpop(5, PubConfig.MonitorListKey);
            if (result == null) {
                break;
            }
            latch.countDown();
            logger.info(String.valueOf(result));
            InvokeDO invokeDO = (InvokeDO) FSTSeriazle.deserialize(StringUtil.stringToByte(result));
            logger.info(String.valueOf(FastJsonUtil.serialize(invokeDO)));
            listUrl.add(invokeDO);
        }
    }
}
