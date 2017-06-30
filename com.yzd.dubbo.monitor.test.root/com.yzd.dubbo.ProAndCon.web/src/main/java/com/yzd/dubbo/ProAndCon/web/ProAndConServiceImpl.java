package com.yzd.dubbo.ProAndCon.web;

import com.yzd.dubbo.service.inf.ProAndConServiceInf;
import com.yzd.dubbo.service.inf.ProviderServiceInf;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zd.yao on 2017/6/28.
 */
public class ProAndConServiceImpl implements ProAndConServiceInf {
    @Override
    public String callProAndConServiceInf() {
        return "hello-callProAndConServiceInf";
    }
    @Autowired
    ProviderServiceInf providerServiceInf;
    @Override
    public String callProByProAndConServiceInf() {
        return "hello-callProByProAndConServiceInf=="+providerServiceInf.callProviderServiceInf();
    }
}
