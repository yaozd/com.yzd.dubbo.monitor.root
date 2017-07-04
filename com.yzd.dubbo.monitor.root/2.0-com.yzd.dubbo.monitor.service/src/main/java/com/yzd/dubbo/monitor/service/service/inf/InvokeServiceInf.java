package com.yzd.dubbo.monitor.service.service.inf;

import com.yzd.dubbo.monitor.service.dao.entity.InvokeDO;

import java.util.List;

/**
 * Created by zd.yao on 2017/6/30.
 */
public interface InvokeServiceInf {
    void addRecordBatch (List<InvokeDO> recordList);
}
