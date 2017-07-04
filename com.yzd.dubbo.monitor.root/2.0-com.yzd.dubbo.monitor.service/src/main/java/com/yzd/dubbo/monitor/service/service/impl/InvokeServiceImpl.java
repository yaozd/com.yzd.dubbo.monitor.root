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

    @Override
    public void addRecordBatch(List<InvokeDO> recordList) {
        invokeDOMapper.addRecordBatch(recordList);
    }
}
