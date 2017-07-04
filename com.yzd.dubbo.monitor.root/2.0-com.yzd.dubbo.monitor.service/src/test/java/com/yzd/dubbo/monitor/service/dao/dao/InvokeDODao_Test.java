package com.yzd.dubbo.monitor.service.dao.dao;

import com.yzd.dubbo.monitor.common.uuid.UUIDGenerator;
import com.yzd.dubbo.monitor.service.dao.base._BaseUnitTest;
import com.yzd.dubbo.monitor.service.dao.entity.InvokeDO;
import com.yzd.dubbo.monitor.service.dao.mapper.InvokeDOMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zd.yao on 2017/6/30.
 */
public class InvokeDODao_Test extends _BaseUnitTest {

    @Autowired
    InvokeDOMapper invokeDOMapper;

    @Test
    public void selectByPrimaryKey() {
        invokeDOMapper.selectByPrimaryKey("1");
    }
    @Test
    public void insertSelective() {
        InvokeDO record =new InvokeDO();
        record.setUuId(UUIDGenerator.getUUID());
        invokeDOMapper.insertSelective(record);
    }
    @Test
    public void addRecordBatch() {
        InvokeDO record =new InvokeDO();
        record.setUuId(UUIDGenerator.getUUID());
        record.setApplication("application");
        record.setConsumerHost("192.168.3.45");
        record.setConsumerPort("0");
        record.setProviderHost("192.168.3.45");
        record.setProviderPort("29016");
        List<InvokeDO>invokeDOList=new ArrayList<>();
        invokeDOList.add(record);
        invokeDOMapper.addRecordBatch(invokeDOList);
    }
}
