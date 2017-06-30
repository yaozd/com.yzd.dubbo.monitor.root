package com.yzd.dubbo.provider.web;

import com.yzd.dubbo.service.inf.ProviderServiceInf;

/**
 * Created by zd.yao on 2017/6/28.
 */
public class ProviderServiceImpl implements ProviderServiceInf {
    @Override
    public String callProviderServiceInf() {
        return "hello-callProviderServiceInf";
    }
}
