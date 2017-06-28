package com.yzd.dubbo.monitor.service.dao.entity;

import com.yzd.dubbo.monitor.common.seriazle.FSTSeriazle;
import com.yzd.dubbo.monitor.common.stringExt.StringUtil;
import org.junit.Test;

/**
 * Created by zd.yao on 2017/6/28.
 */
public class InvokeDO_Test {
    @Test
    public void serialize(){
        InvokeDO invokeDO=new InvokeDO();
        invokeDO.setApplication("test.monitor");
        String str= StringUtil.byteToString(FSTSeriazle.serialize(invokeDO));
        System.out.println(str);
        InvokeDO obj= (InvokeDO) FSTSeriazle.deserialize(StringUtil.stringToByte(str));
    }
}
