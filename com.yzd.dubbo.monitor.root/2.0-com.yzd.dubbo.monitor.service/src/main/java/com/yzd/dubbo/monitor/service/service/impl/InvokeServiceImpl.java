package com.yzd.dubbo.monitor.service.service.impl;

import com.yzd.dubbo.monitor.service.dao.entity.InvokeDO;
import com.yzd.dubbo.monitor.service.dao.mapper.InvokeDOMapper;
import com.yzd.dubbo.monitor.service.service.inf.InvokeServiceInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zd.yao on 2017/6/30.
 */
@Service
public class InvokeServiceImpl implements InvokeServiceInf {
    @Autowired
    InvokeDOMapper invokeDOMapper;

    /**
     * MyBatis批量插入数据
     * http://chenzhou123520.iteye.com/blog/1583407/
     * 使用batch insert解决MySQL的insert吞吐量问题
     * http://blog.csdn.net/orion61/article/details/32108547
     * 在我这个简单测试场景中，values number最合适的值是50，和单values对比，耗时减少 97% ，insert吞吐量提升 36倍 。
     * @param recordList
     */
    @Override
    public void addRecordBatch(List<InvokeDO> recordList) {
        invokeDOMapper.addRecordBatch(recordList);
    }
}
